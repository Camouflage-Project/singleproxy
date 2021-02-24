import React, {useEffect} from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {baseUrl, getOS, os} from "./util";
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Link from "next/link";

const useStyles = makeStyles(theme => ({
    codeSnippet: {
        color: "black",
        background:"#eaedfa",
        fontSize: 18,
        textAlign: "center",
        marginTop: 25
    },
    content: {color: "black"},
    download: {justifyContent: "center"}
}));

export const DownloadAlertDialog = props => {
    const classes = useStyles()
    const unixInstallContent = "To install, just copy and paste the below code into a terminal window and press enter. Then, proceed to your dashboard."

    const currentOs = getOS()
    let title
    let content
    let unix = false
    let windows = false

    switch (currentOs) {
        case os.linux:
            title = "Linux installation"
            content = unixInstallContent
            unix = true
            break
        case os.macOs:
            title = "MacOS installation"
            content = unixInstallContent
            unix = true
            break
        case os.windows:
            title = "Windows installation"
            content = "Click to download and then right click on the downloaded file and click Run as administrator."
            windows = true
            break
        default:
            title = "Installation"
            content = "AleaLogic SingleProxy is currently not supported on mobile devices. Please access this page on a Windows/MacOS/Linux device to install."
    }

    const unixInstallCommand = `curl -s ${baseUrl}/install?id=${props.token} | sudo bash`

    return (
        <div>
            <Dialog
                open={props.open}
                onClose={props.handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText className={classes.content} id="alert-dialog-description">
                        {content}
                    </DialogContentText>
                    {unix
                        ? <DialogContentText id="alert-dialog-description">
                            <Typography className={classes.codeSnippet}>{unixInstallCommand}</Typography>
                        </DialogContentText>
                        : null}
                </DialogContent>
                {windows
                    ? <DialogActions className={classes.download}>
                        <Button href={baseUrl + "/alealogic-release"} variant="outlined" onClick={props.handleClose} color="primary" autoFocus>
                            Download
                        </Button>
                    </DialogActions>
                    : null}
                <DialogActions>
                    <Link href="/dashboard"><a>
                    <Button onClick={props.handleClose} color="primary" autoFocus>
                        Go to dashboard
                    </Button>
                    </a></Link>
                </DialogActions>
            </Dialog>
        </div>
    );
}
