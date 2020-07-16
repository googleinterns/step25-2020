async function prepareLogin() {
    let login = document.getElementById("login");
    let loginButton = document.getElementById("login-button");
    let logoutButton = document.getElementById("logout-button");
    let coursesButton = document.getElementById("goto-courses-button");
    let loginActionText = document.getElementById("login-action-text");

    loginButton.hidden = true;
    logoutButton.hidden = true;
    coursesButton.hidden = true;

    const servletURL = "/login";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let loginURL = response.headers.get("login-url");
    let logoutURL = response.headers.get("logout-url");
    let isLoggedIn = response.headers.get("is-logged-in");
    
    if (isLoggedIn === "true") {
        logoutButton.hidden = false;
        logoutButton.href = logoutURL;
        login.href = "/pages/courses.html";
        loginActionText.innerText = "Continue to your Courses";
    } else if (isLoggedIn === "false") {
        login.href = loginURL;
        loginButton.hidden = false;
        loginButton.href = loginURL;
        loginActionText.innerText = "Log in with Google";
    }
}

prepareLogin();