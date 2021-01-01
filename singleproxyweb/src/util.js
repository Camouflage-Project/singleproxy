import axios from "axios";

export const setSessionTokenInCookie = token => document.cookie = "token=" + token

export const getSessionTokenFromCookie = () => {
    const nameEQ = "token=";
    const ca = document.cookie.split(';');
    for(let i=0;i < ca.length;i++) {
        let c = ca[i];
        while (c.charAt(0)===' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

export const fetchSessionToken = () => {
    axios.post(baseUrl + "/token", {"os": getOS()}, {withCredentials: true})
        .then(res => setSessionTokenInCookie(res.data));
};

export const getOS = () => {
    let userAgent = window.navigator.userAgent,
        platform = window.navigator.platform,
        macosPlatforms = ['Macintosh', 'MacIntel', 'MacPPC', 'Mac68K', 'darwin'],
        windowsPlatforms = ['Win32', 'Win64', 'Windows', 'WinCE'],
        iosPlatforms = ['iPhone', 'iPad', 'iPod']

    if (macosPlatforms.indexOf(platform) !== -1) {
        return os.macOs
    } else if (iosPlatforms.indexOf(platform) !== -1) {
        return os.iOs
    } else if (windowsPlatforms.indexOf(platform) !== -1) {
        return os.windows
    } else if (/Android/.test(userAgent)) {
        return os.android
    } else if (/Linux/.test(platform)) {
        return os.linux
    }

    return os;
}

export const os = {
    macOs: "MACOS",
    iOs: "IOS",
    windows: "WINDOWS",
    android: "ANDROID",
    linux: "LINUX"
}

export const baseUrl = "http://localhost:8080/api"