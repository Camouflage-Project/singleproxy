import React, {useEffect} from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {getOS, os} from "./util";
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

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

export const AlertDialog = (props) => {
    const classes = useStyles()
    const [title, setTitle] = React.useState("")
    const [content, setContent] = React.useState("")
    const [unix, setUnix] = React.useState(false)
    const [windows, setWindows] = React.useState(false)

    const unixInstallContent = "To install, just copy and paste the below line into a terminal window and press enter. Then, proceed to your dashboard."

    useEffect(() => {
        let currentOs = getOS();
        if (currentOs === os.linux) {
            setTitle("Linux installation")
            setContent(unixInstallContent)
            setUnix(true)
        }else if (currentOs === os.windows) {
            setTitle("MacOS installation")
            setContent(unixInstallContent)
            setUnix(true)
        } else if (currentOs === os.windows) {
            setTitle("Windows installation")
            setContent("Click to download and then right click on the downloaded file and click Run as administrator.")
            setWindows(true)
        } else {
            setTitle("Installation")
            setContent("AleaLogic SingleProxy is currently not supported on mobile devices. Please access this page on a Windows/MacOS/Linux device to install.")
        }
    });

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
                            <Typography className={classes.codeSnippet}>curl http://localhost:8080/api/alealogic-release | sudo bash</Typography>
                        </DialogContentText>
                        : null}
                </DialogContent>
                {windows
                    ? <DialogActions className={classes.download}>
                        <Button href={"http://localhost:8080/api/alealogic-release"} variant="outlined" onClick={props.handleClose} color="primary" autoFocus>
                            Download
                        </Button>
                    </DialogActions>
                    : <DialogActions className={classes.download}>
                        <Button href={"http://localhost:8080/api/alealogic-release"} variant="outlined" onClick={props.handleClose} color="primary" autoFocus>
                            Download
                        </Button>
                    </DialogActions>}
                <DialogActions>
                    <Button onClick={props.handleClose} color="primary" autoFocus>
                        Go to dashboard
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}
