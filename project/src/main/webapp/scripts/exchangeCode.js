async function exchangeCode() {
    const servletURL = "/exchangeCode";
    
    let response = await fetch(servletURL);
    
    let nextPage = response.headers.get("next-page");

    window.location.replace(nextPage);
}

exchangeCode();