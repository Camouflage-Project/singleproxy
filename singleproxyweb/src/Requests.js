import React, {useEffect} from 'react';
import Link from '@material-ui/core/Link';
import {makeStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Title from './Title';
import {useRouter} from "next/router";
import {baseUrl, fetchAndApplyOrLogin, getSessionTokenFromCookie} from "./util";
import axios from "axios";

const useStyles = makeStyles((theme) => ({
    seeMore: {
        marginTop: theme.spacing(3),
    },
}));

export default function Requests() {
    const classes = useStyles();
    const router = useRouter();

    const [requestTimestamps, setRequestTimestamps] = React.useState([]);
    const [page, setPage] = React.useState(0)

    const fetchRequestTimestamps = () => {
        axios.get(baseUrl + `/request-timestamps/${page}`, {
            headers: {
                'token': getSessionTokenFromCookie()
            }
        })
            .then(res => setRequestTimestamps(res.data))
            .catch(_ => router.push("/login"))
    }

    const incrementPageAndFetch = () => {
        setPage(page + 1)
        fetchRequestTimestamps()
    }

    useEffect(fetchRequestTimestamps,requestTimestamps)

    return (
        <React.Fragment>
            <Title>Requests</Title>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>#</TableCell>
                        <TableCell>Timestamp</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {/*{requestTimestamps.map((t, i) => (*/}
                    {/*    <TableRow key={i}>*/}
                    {/*        <TableCell>{i}</TableCell>*/}
                    {/*        <TableCell>{t}</TableCell>*/}
                    {/*    </TableRow>*/}
                    {/*))}*/}
                </TableBody>
            </Table>
            <div className={classes.seeMore}>
                <Link color="primary" href="#" onClick={incrementPageAndFetch}>
                    See next requests
                </Link>
            </div>
        </React.Fragment>
    );
}
