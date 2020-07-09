var pdf_url = '../images/Question-1-Student.pdf'
// initialize and load the PDF

async function loadPDF(pdf_url) {
  var pdf_url = '../images/Question-1-Student.pdf'; // hard coded -- i dont know how to get the parameter url down into a url to use in the try block wtf

    // get handle of pdf document
    try {
        _PDF_DOC = await pdfjsLib.getDocument({ url: pdf_url });
    }
    catch(error) {
      // pdf_content.innerHTML("ERROREREREREREREREREREERERER");
        alert(error.message);
    }

    // show the first page
  renderPage(1);
}

// load and render specific page of the PDF onto canvas
async function renderPage(page_no) {
    _PAGE_RENDERING_IN_PROGRESS = 1;
    _CURRENT_PAGE = page_no;
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

  drawRect();
}








function drawRect() {

}




function fuck() {
  var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");

var canvasOffset = $("#canvas").offset();
var offsetX = canvasOffset.left;
var offsetY = canvasOffset.top;

var isDrawing = false;
var startX;
var startY;

    mouseX = parseInt(e.clientX - offsetX);
    mouseY = parseInt(e.clientY - offsetY);
    $("#downlog").html("Down: " + mouseX + " / " + mouseY);

    // Put your mousedown stuff here
    if (isDrawing) {
        isDrawing = false;
        ctx.beginPath();
        ctx.rect(startX, startY, mouseX - startX, mouseY - startY);
        ctx.fill();
        canvas.style.cursor = "default";
    } else {
        isDrawing = true;
        startX = mouseX;
        startY = mouseY;
        canvas.style.cursor = "crosshair";
    }
}

$("#canvas").mousedown(function (e) {
    handleMouseDown(e);
});