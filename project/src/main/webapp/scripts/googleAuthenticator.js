async function requestAccessTokenReceiveAuthCode() {
    const servletURL = "/requestAccessToken";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

async function exchangeAuthCodeForAccessToken() {
    const servletURL = "/exchangeAuthCode";
    
    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });
    
    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

function checkState() {
    var url = window.location.href;

    if (url.indexOf("?state=auth_code_received") != -1) {
        return true;
    } else {
        return false;
    }
}

if (checkState()) {
    exchangeAuthCodeForAccessToken();
} else {
    requestAccessTokenReceiveAuthCode();
}
