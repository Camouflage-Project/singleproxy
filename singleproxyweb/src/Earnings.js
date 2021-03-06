import React, {useEffect} from 'react';
import Typography from '@material-ui/core/Typography';
import Title from './Title';
import axios from "axios";
import {baseUrl, getSessionTokenFromCookie} from "./util";
import {useRouter} from "next/router";

export default function Earnings() {
    const router = useRouter();

    const [tokenBits, setTokenBits] = React.useState("");

    const fetchEarnings = () => {
        axios.get(baseUrl + "/earnings", {
            headers: {
                'token': getSessionTokenFromCookie()
            }
        })
            .then(res => setTokenBits(res.data))
            .catch(_ => router.push("/login"))
    }

    useEffect(fetchEarnings)

    return (
        <React.Fragment>
            <Title>Earnings</Title>
            <Typography component="p" variant="h5">
                {tokenBits} token bits
            </Typography>
        </React.Fragment>
    );
}
