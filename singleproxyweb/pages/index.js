import React, {useEffect} from 'react';
import initiateBackgroundEffect from '../src/background'
import CssBaseline from "@material-ui/core/CssBaseline";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Link from "next/link";
import Button from "@material-ui/core/Button";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import Box from "@material-ui/core/Box";
import Copyright from "../src/Copyright";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    '@global': {
        ul: {
            margin: 0,
            padding: 0,
            listStyle: 'none',
        },
    },
    appBar: {
        borderBottom: `1px solid ${theme.palette.divider}`,
    },
    toolbar: {
        flexWrap: 'wrap',
    },
    toolbarTitle: {
        flexGrow: 1,
    },
    centerTitle: {
        padding: theme.spacing(0, 0, 2),
    },
    link: {
        margin: theme.spacing(1, 1.5),
    },
    heroContent: {
        padding: theme.spacing(8, 0, 6),
    },
    cardHeader: {
        backgroundColor:
            theme.palette.type === 'light' ? theme.palette.grey[200] : theme.palette.grey[700],
    },
    cardPricing: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'baseline',
        marginBottom: theme.spacing(2),
    },
    footer: {
        borderTop: `1px solid ${theme.palette.divider}`,
        marginTop: theme.spacing(8),
        paddingTop: theme.spacing(3),
        paddingBottom: theme.spacing(3),
        [theme.breakpoints.up('sm')]: {
            paddingTop: theme.spacing(6),
            paddingBottom: theme.spacing(6),
        },
    },
    centered: {
        justifyContent: 'center',
        alignItems: "center",
        direction: "column",
        justify: "center"
    }
}));

export default function Home() {
    const classes = useStyles();

    useEffect(() => {
        initiateBackgroundEffect();
    });

  return (
      <>
          <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
            <defs>
              <filter id="goo">
                <feGaussianBlur in="SourceGraphic" stdDeviation="10" result="blur" />
                <feColorMatrix in="blur" mode="matrix" values="1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 19 -9" result="goo" />
                <feComposite in="SourceGraphic" in2="goo" operator="atop"/>
              </filter>
            </defs>
          </svg>
          <canvas id="canvas1"/>
          <canvas id="canvas2"/>

          <CssBaseline />
          <AppBar position="static" color="default" elevation={0} className={classes.appBar}>
              <Toolbar className={classes.toolbar}>
                  <Typography variant="h5" color="inherit" noWrap className={classes.toolbarTitle}>
                      A L
                  </Typography>
                  <Link href="/login"><a>
                      <Button href="#" color="primary" variant="outlined" className={classes.link}>
                          Login
                      </Button>
                  </a></Link>
              </Toolbar>
          </AppBar>
          <Grid
              container
              spacing={0}
              direction="column"
              alignItems="center"
              justify="center"
              style={{ minHeight: '70vh' }}
          >
          <Container maxWidth="sm" component="main" className={classes.heroContent}>
              <Typography className={classes.centerTitle} component="h1" variant="h2" align="center" color="textPrimary" gutterBottom>
                  AleaLogic
              </Typography>
              <Typography variant="h5" align="center" color="textSecondary" component="p">
                  Earn money by sharing your Internet bandwidth or your graphics card in just two clicks.
              </Typography>
          </Container>
          <Grid container maxWidth="md" component="main" className={classes.centered}>
              <Button variant={"outlined"} color="primary" size="large">
                  Here's how
              </Button>
          </Grid>
          </Grid>
          <Container maxWidth="md" component="footer" className={classes.footer}>
              <Box mt={5}>
                  <Copyright />
              </Box>
          </Container>
      </>
  )
}
