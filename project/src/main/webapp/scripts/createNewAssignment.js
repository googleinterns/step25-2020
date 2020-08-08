async function createNewAssignment() {
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const maxPoints = document.getElementById("maxPoints").value;
    const dueDate = document.getElementById("dueDate").value;
    const dueTime = document.getElementById("dueTime").value;

    const yearMonthDayHoursMinutes = getYearMonthDayHoursMinutes(dueDate, dueTime);

    const year = yearMonthDayHoursMinutes[0];
    const month = yearMonthDayHoursMinutes[1];
    const day = yearMonthDayHoursMinutes[2];
    const hours = yearMonthDayHoursMinutes[3];
    const minutes = yearMonthDayHoursMinutes[4]; 

    let servletURL = `/createNewAssignment?title=${title}&description=${description}&maxPoints=${maxPoints}&year=${year}&month=${month}&day=${day}&hours=${hours}&minutes=${minutes}`;

    let response = await fetch(servletURL, {
        method: "POST",
        mode: "no-cors"
    });

    let redirect = response.headers.get("redirect");

    if (redirect != null) {
        window.location.replace(redirect);
    } else {
        let responseCode = response.headers.get("responseCode");

        if (responseCode == 200) {
            location.reload();
        } else {
            // TODO: Handle error response
        }
    }
}

function getYearMonthDayHoursMinutes(dueDate, dueTime) {
    const yearMonthDay = getYearMonthDay(dueDate);
    const hoursMinutes = getHoursMinutes(dueTime);

    const year = yearMonthDay[0];
    const month = yearMonthDay[1];
    const day = yearMonthDay[2];
    const hours = hoursMinutes[0]
    const minutes = hoursMinutes[1];

    let now = new Date();
    let offset = now.getTimezoneOffset();
    let hoursOffset = offset / 60;

    let dueTimeAndDate = new Date(year, month, day, hours, minutes, 0, 0);
    dueTimeAndDate.setHours(dueTimeAndDate.getHours() + hoursOffset);

    return [dueTimeAndDate.getFullYear(), dueTimeAndDate.getMonth(), dueTimeAndDate.getDate(), dueTimeAndDate.getHours(), dueTimeAndDate.getMinutes()];
}

function getYearMonthDay(date) {
    let firstSeperator = date.indexOf('-');
    let secondSeperator = date.lastIndexOf('-');

    return [
        parseInt(date.substring(0, firstSeperator), 10),
        parseInt(date.substring(firstSeperator + 1, secondSeperator), 10),
        parseInt(date.substring(secondSeperator + 1), 10)
    ];
}

function getHoursMinutes(time) {
    let seperator = time.indexOf(':');

    return [
        parseInt(time.substring(0, seperator), 10),
        parseInt(time.substring(seperator + 1), 10)
    ];
}