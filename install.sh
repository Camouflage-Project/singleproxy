#!/bin/bash

echo "$(whoami)"

[ "$UID" -eq 0 ] || sudo "$0" "$@"

case $(uname -s) in
    Linux) os="linux" ;;
    *) os="not_linux" ;;
esac

if [ "$os" = "not_linux" ]; then
    echo "Unsupported OS $(uname -s). Only Linux binaries are available."
	exit
fi

case $(uname -m) in
x86_64) arch="x86_64" ;;
*) arch="other" ;;
esac

if [ "$arch" = "other" ]; then
	echo "Unsupported architecture $(uname -m). Only x64 binaries are available."
	exit
fi

test_program_installed () {
    if command -v $1 > /dev/null; then
        echo "Program $1 successfully installed"
    else
        echo "Program $1 not installed"
        exit 1
    fi
}

install_docker() {
    # Uninstall old versions
    apt-get remove -qq docker docker-engine docker.io containerd runc
    # Update
    apt-get update -qq
    # Install packages to allow apt to use a repository over HTTPS
    apt-get -qq install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
    # Add Docker’s official GPG key
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
    # Add repository
    add-apt-repository -y \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"
    # Update
    apt-get -qq update
    apt-get -qq install docker-ce docker-ce-cli containerd.io
}

check_docker_version() {
    req_ver='19.03.0'
    installed_ver=$(docker --version | cut -f3 -d ' ' | sed 's/,//g')

    if [[ "$req_ver" < "$installed_ver" || "$req_ver" == $installed_ver ]]; then
        echo "Docker version ${installed_ver} is suitable - minimal required version ${req_ver}."
    else
        echo "Docker version ${installed_ver} is not suitable - minimal required version ${req_ver}."
        exit 1
    fi
}

install_nvidia_docker() {
    # Install nvidia-docker2 for nomad
    sudo apt-get install nvidia-docker2

    # Add the package repositories
    distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
    curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | sudo apt-key add -
    curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | sudo tee /etc/apt/sources.list.d/nvidia-docker.list

    sudo apt-get update && sudo apt-get install -y nvidia-container-toolkit
    sudo systemctl restart docker
}

install_nvidia_drivers() {
    if lspci | grep -i nvidia > /dev/null; then
        echo "NVIDIA GPU available"
    else
        echo "NVIDIA GPU is missing"
        exit 1
    fi

    distro='ubuntu'
    arch='x86_64'
    version='1804'

    case $(uname -m && cat /etc/*release) in
    *Ubuntu*20.10*|*Ubuntu*20.04*) version='2004';;
    *Ubuntu*18.10*) version='1810';;
    *Ubuntu*18.04*) version='1804';;
    *Ubuntu*16.04*) version='1604';;
    *Ubuntu*14.04*) version='1404';;
    *) echo "Unsupported version of OS"; exit 1;;
    esac

    if command -v gcc --version > /dev/null; then
        echo "Program gcc --version successfully installed"
    else
        echo "Program gcc --version not installed. Installing"
        sudo apt -qq -y install gcc
    fi
    
    sudo apt-get -qq -y install linux-headers-$(uname -r)

    sudo wget "https://developer.download.nvidia.com/compute/cuda/repos/$distro$version/$arch/cuda-$distro$version.pin"
    sudo mv "cuda-$distro$version.pin" /etc/apt/preferences.d/cuda-repository-pin-600
    sudo apt-key adv -qq --fetch-keys "https://developer.download.nvidia.com/compute/cuda/repos/$distro$version/$arch/7fa2af80.pub"
    sudo add-apt-repository "deb http://developer.download.nvidia.com/compute/cuda/repos/$distro$version/$arch/ /"
    sudo apt-get update -qq
    sudo apt-get -y -qq install cuda-drivers
    sudo systemctl restart docker
}


install_docker
check_docker_version
install_nvidia_drivers
install_nvidia_docker

systemctl daemon-reload

echo "Installation complete"
