import React, {useEffect} from 'react';
import Title from './Title';
import axios from "axios";
import {baseUrl, getSessionTokenFromCookie} from "./util";
import {useRouter} from 'next/router'
import Typography from "@material-ui/core/Typography";

export default function Status() {
    const router = useRouter();

    const [status, setStatus] = React.useState("inactive");

    const fetchProxyStatus = () => {
        axios.get(baseUrl + "/proxy-status", {
            headers: {
                'token': getSessionTokenFromCookie()
            }
        })
            .then(res => {
                res.data === true ? setStatus("active") : setStatus("inactive")
            })
            .catch(_ => router.push("/login"))
    }

    useEffect(fetchProxyStatus, [])

    return (
        <React.Fragment>
            <Title>Proxy status</Title>
            <Typography variant="h5">{status}</Typography>
        </React.Fragment>
    );
}
