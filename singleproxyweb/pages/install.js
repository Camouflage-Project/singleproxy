import Head from 'next/head'
import styles from '../styles/Home.module.css'
import Link from 'next/link'

export default function Install() {
  return (
    <div className={styles.container}>
      <Head>
        <title>Install</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className={styles.main}>
        <h1 className={styles.title}>
          Install
        </h1>
        <p style={{fontSize:20}}>
            For macOS and Linux distributions, open a terminal window, paste
        </p>
        <p style={{fontSize:20}}>
          <code className={styles.code}>curl http://localhost:8080/api/download-release | sudo bash </code> and press enter.
        </p>
        <p style={{fontSize:20}}>
            Afterwards, just refresh this page to go to your dashboard.
        </p>
        <p style={{fontSize:20}}>
            To install on Windows, click <a href="http://localhost:8080/api/alealogic-release"> here </a> and then right click the downloaded file and "Run as administrator"
        </p>
        <p style={{fontSize:20}}>
            Afterwards, just refresh this page to go to your dashboard.
        </p>
      </main>
    </div>
  )
}
