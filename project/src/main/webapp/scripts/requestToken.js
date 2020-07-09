async function requestToken() {
    const servletURL = "/requestToken";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

requestToken();