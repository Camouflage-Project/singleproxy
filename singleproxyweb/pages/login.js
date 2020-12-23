import React from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import axios from 'axios';
import {baseUrl, setSessionTokenInCookie} from "../src/util";
import {useRouter} from "next/router";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            {'AleaLogic '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));

export default function login() {
    const router = useRouter();
    const [key, setKey] = React.useState("")
    const [loginError, setLoginError] = React.useState(false)
    const classes = useStyles();

    const submit = () => {
        axios.post(baseUrl + "/api-key-login", {"apiKey": key})
            .then(res => {
                setSessionTokenInCookie(res.data)
                setLoginError(false)
                router.push('/dashboard', undefined, {shallow: true})
            })
            .catch(err => {
                if (err.response) {
                    setLoginError(true)
                }
            })
    };

    const handleKeyChange = e => {
        setKey(e.target.value)
    };

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon/>
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <form className={classes.form} noValidate>
                    {loginError
                        ?
                        <TextField
                            error
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Key"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            value={key}
                            onChange={handleKeyChange}
                        />
                        :
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Key"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            value={key}
                            onChange={handleKeyChange}
                        />
                    }
                    <Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                        onClick={submit}
                    >
                        Sign In
                    </Button>
                </form>
            </div>
            <Box mt={8}>
                <Copyright/>
            </Box>
        </Container>
    );
}
