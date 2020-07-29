var currentPage;
var totalPages;
var pdf_url = '/images/5-pages-submission.pdf';
// initialize and load the PDF


async function loadPDF(pdf_url) {
  var pdf_url = '/images/5-pages-submission.pdf'; 
  // hard coded -- i dont know how to get the parameter url down into a url to use in the try block wtf??

    // get handle of pdf document
    try {
        _PDF_DOC = await pdfjsLib.getDocument({ url: pdf_url });
    }
    catch(error) {
        alert(error.message);
    }
    
    totalPages = _PDF_DOC.numPages;

    // show the first page
  renderPage(1);
}

// load and render specific page of the PDF onto canvas
async function renderPage(page_no) {
    _PAGE_RENDERING_IN_PROGRESS = 1;
    currentPage = page_no;
    var pageNum = document.getElementById('pageNum');
    
    // show current pagenum of assignment
    pageNum.innerHTML = "Page " + currentPage + " of " + totalPages;

    var pdf_content = document.getElementById('pdfContent'),
      _CANVAS = document.querySelector('#pdfCanvas');;

    // get handle of page
    try {
      _PDF_DOC = await pdfjsLib.getDocument({ url: pdf_url });
        var page = await _PDF_DOC.getPage(page_no);
    }
    catch(error) {
        alert(error.message);
    }

    // original width of the pdf page at scale 1
    var pdf_original_width = page.getViewport(1).width;
  
    // as the canvas is of a fixed width we need to adjust the scale of the viewport where page is rendered
    var scale_required = _CANVAS.width / pdf_original_width;

    // get viewport to render the page at required scale
    var viewport = page.getViewport(scale_required);

    // set canvas height same as viewport height
    _CANVAS.height = viewport.height;

    // page is rendered on <canvas> element
    var render_context = {
        canvasContext: _CANVAS.getContext('2d'),
        viewport: viewport
        
    };
        
    // render the page contents in the canvas
    try {
        await page.render(render_context);
    }
    catch(error) {
        alert(error.message);
    }

  mousePositions();
}

// click on the "Previous" page button
function prevButton() {
  if (currentPage != 1) {
    renderPage(--currentPage);
  }
}

// click on the "Previous" page button
function nextButton() {
  if (currentPage != totalPages) {
    renderPage(++currentPage);
  }
}

var leftXCoord, topYCoord, rightXCoord, lowerYCoord;

function mousePositions() {

  function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
      x: evt.clientX - rect.left,
      y: evt.clientY - rect.top
    };
  }
  var canvas = document.getElementById('pdfCanvas');
  var context = canvas.getContext('2d');
  var locations = document.getElementById('myCanvas');

  canvas.addEventListener('click', function(evt) {
    var mousePos = getMousePos(canvas, evt);
    var x = mousePos.x.toFixed(0);
    var y = mousePos.y.toFixed(0);
    writePos(x, y);
  }, false);

  let corner = "left"; // variable to alternate which corner's location is being registered
  var questionName, questionPoints;
  function writePos(x, y) {
    var upperLeft = document.getElementById('upperLeft');
    var lowerRight = document.getElementById('lowerRight');
    var setCorner = document.getElementById('setCorner');

    if (corner == "left") {
      leftXCoord = x;
      topYCoord = y;
      upperLeft.innerHTML = "Upper Left Corner: " + x + ", " + y;
      corner = "right";
      setCorner.innerHTML = "Click Lower Right corner";
    } else {
      rightXCoord = x;
      lowerYCoord = y;
      lowerRight.innerHTML = "Lower Right Corner: " + x + ", " + y;
      corner = "left";
      setCorner.innerHTML = "Click Upper Left corner if it's inaccurate";
      crop(leftXCoord, topYCoord, rightXCoord, lowerYCoord);
    }

  }

}

function crop(leftXCoord, topYCoord, rightXCoord, lowerYCoord) {
  var cropX = leftXCoord;
  var cropY = topYCoord;
  var cropWidth = rightXCoord-leftXCoord;
  var cropHeight = lowerYCoord-topYCoord;
  var pdfCanvas = document.getElementById("pdfCanvas");

  var cropCanvas = document.querySelector('#cropCanvas');
  cropCanvas.height = cropHeight;
  cropCanvas.width = cropWidth;
  
  cropCanvas = document.getElementById("cropCanvas").getContext('2d');

  cropCanvas.drawImage(pdfCanvas, cropX, cropY, cropWidth, cropHeight, 0, 0, cropWidth, cropHeight);
}

// onclick called when "Add Question" button is clicked
function submitBoxButton() {
  const assignmentKey = getUrlVars()['assignment-key'];
  var qName = document.getElementById('question-name').value;
  var qPoints = document.getElementById('question-points').value;
  var qType;
  if (document.getElementById('multiple-choice').checked){
      qType="multiple-choice";
  } else if (document.getElementById('short-answer').checked){
      qType="short-answer";
  } else {
      qType="other";
  }

  if (leftXCoord != undefined && rightXCoord != undefined && qName != ""){
    var url = `/ManageBox?qName=${qName}&qType=${qType}&qPoints=${qPoints}&assignment-key=${assignmentKey}&leftXCoord=${leftXCoord}&topYCoord=${topYCoord}&rightXCoord=${rightXCoord}&lowerYCoord=${lowerYCoord}`;
    fetch(url, {method:"POST"});
  }
}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}