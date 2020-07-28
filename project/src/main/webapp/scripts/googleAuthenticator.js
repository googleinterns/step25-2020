async function requestAccessTokenReceiveAuthCode(baseURL) {
    const servletURL = `/requestAccessToken?baseURL=${baseURL}`;

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

async function exchangeAuthCodeForAccessToken(baseURL) {
    const servletURL = `/exchangeAuthCode?baseURL=${baseURL}`;
    
    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });
    
    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

async function checkState() {
    const baseURL = window.location.protocol + "//" + window.location.hostname;
    const url = window.location.href;

    if (url.indexOf("?state=auth_code_received") == -1) {
        requestAccessTokenReceiveAuthCode(baseURL);
    } else {
        exchangeAuthCodeForAccessToken(baseURL);
    }
}

checkState();