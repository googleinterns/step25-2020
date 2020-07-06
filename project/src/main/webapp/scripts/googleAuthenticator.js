async function requestToken() {
    const servletURL = "/requestToken";

    await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });
}
