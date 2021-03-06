import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import axios from "axios";
import {baseUrl, getSessionTokenFromCookie} from "./util";
import {useRouter} from "next/router";

const useStyles = makeStyles(theme => ({
    codeSnippet: {
        color: "black",
        background: "#eaedfa",
        fontSize: 18,
        textAlign: "center",
        marginTop: 25
    },
    content: {color: "black"},
    download: {justifyContent: "center"}
}));

export const AccountKeyAlertDialog = props => {
    const classes = useStyles()
    const router = useRouter();

    const acceptAccountKey = () => {
        axios.post(baseUrl + "/accept-api-key", null, {
            headers: {
                'token': getSessionTokenFromCookie()
            }
        })
            .catch(_ => router.push("/login"))
    }

    const handleClose = () => {
        acceptAccountKey()
        props.handleClose()
    };

    return (
        <div>
            <Dialog
                open={props.open}
                onClose={props.handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">Account Key</DialogTitle>
                <DialogContent>
                    <DialogContentText className={classes.content} id="alert-dialog-description">
                        Save this account key since you will need it for logging into your account!
                    </DialogContentText>
                    <DialogContentText id="alert-dialog-description">
                        <Typography className={classes.codeSnippet}>{props.apiKey}</Typography>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary" autoFocus>
                        Ok
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}
