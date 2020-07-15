async function getAssignments() {
    var servletURL = "/assignment";
    const response = await fetch(servletURL);
    const assignments = await response.json();    
    var myTableDiv = document.getElementById("assignments-container");

    var table = document.createElement('TABLE');

    var tableBody = document.createElement('TBODY');
    var headerTr = document.createElement('TR');
    var nameHeader = document.createElement('TH');
    var statusHeader = document.createElement('TH');
    var pointsHeader = document.createElement('TH');
    nameHeader.innerHTML = "NAME";
    statusHeader.innerHTML = "STATUS";
    pointsHeader.innerHTML = "POINTS";

    headerTr.appendChild(nameHeader);
    headerTr.appendChild(statusHeader);
    headerTr.appendChild(pointsHeader);
    table.appendChild(headerTr);
    table.appendChild(tableBody);

    assignments.forEach(assignment => {
        var tr = document.createElement('TR');
        tableBody.appendChild(tr);

        var link = document.createElement('A');
        var tdName = document.createElement('TD');
        tdName.innerHTML = assignment.name;
        link.appendChild(tdName);
        link.href = "/pages/assignment.html?assignment-key=" + assignment.key;

        var tdStatus = document.createElement('TD');
        tdStatus.innerHTML = assignment.status;
        var tdPoints = document.createElement('TD');
        tdPoints.innerHTML = assignment.points;
        tr.appendChild(link);
        tr.appendChild(tdStatus);
        tr.appendChild(tdPoints);
    });

    myTableDiv.appendChild(table);
}
