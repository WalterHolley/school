//HW1.js
/*
Creates a single, random triangle and
draws it to an HTML canvas.
*/
//vertex shader source code.  renders the triangle's points to the canvas
let VSHADER = `#version 300 es
  in vec4 attribPosition;
  in vec4 attribColor;
  out vec4 vertColor;
  void main(){
    gl_Position = attribPosition;
    vertColor = attribColor;
  }`;

//fragment shader source code. renders triangle colors to the canvas
let FSHADER = `#version 300 es
  precision mediump float;
  in vec4 vertColor;
  out vec4 fragColor;
  void main(){
    fragColor = vertColor;
  }`;

function main(){
  var canvas = document.getElementById("canvas");
  let gl = canvas.getContext("webgl2");

  //check for available webGL.  end program if unavilable
  if(!gl){
    console.log("WebGL2 is unavilable in this browser.")
  }
  else{
    //setup shaders. NOTE: This function comes from webgl utils.  not part of webgl API
    initShaders(gl, VSHADER, FSHADER);

    //setup vertex buffers
    initVBuffers(gl);

    //set color for empty canvas
    gl.clearColor(0.0, 0.0, 0.0, 1.0);

    //clear canvas
    gl.clear(gl.COLOR_BUFFER_BIT);

    //draw
    gl.drawArrays(gl.TRIANGLES, 0, 3);

  }

  //Initializes the vertex buffer
  function initVBuffers(gl){
    //create triangle points and colors
    var triangleProperties = new Float32Array(15);
    for(var i = 0; i < 15; i++){
      triangleProperties[i] = Math.random();
    }

    //create data buffer
    var vertexBuffer = gl.createBuffer();

    //bind buffer to triangle properties
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, triangleProperties, gl.STATIC_DRAW);

    var valSize = Float32Array.BYTES_PER_ELEMENT;

    //describes how the position data should be accessed from the buffer
    var attribPosition = gl.getAttribLocation(gl.program, 'attribPosition');
    gl.vertexAttribPointer(attribPosition, 2, gl.FLOAT, false, valSize * 5, 0);
    gl.enableVertexAttribArray(attribPosition);

    //describes how the color data should be accessed from the buffer
    var attribColor= gl.getAttribLocation(gl.program, 'attribColor');
    gl.vertexAttribPointer(attribColor, 3, gl.FLOAT, false, valSize * 5, valSize * 2);
    gl.enableVertexAttribArray(attribColor);

    //release vertex buffer
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

  }
}
