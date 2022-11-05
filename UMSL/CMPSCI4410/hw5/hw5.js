/**
CMPSCI4410
Homework 5: Cylinder
Author: Walter Holley III
Draws a cylinder using webgl 2
**/

var VSHADER_SOURCE = `#version 300 es
  in vec4 a_Position;   
  uniform mat4 u_MvpMatrix;

  void main() {
    gl_Position = u_MvpMatrix * a_Position;     
  }
`;

var FSHADER_SOURCE =  `#version 300 es
  precision mediump float;

  out vec4 cg_FragColor;
  
  void main() {
    cg_FragColor = vec4(1.0, 0.0, 1.0, 1.0);
  }
`;

  const RES = 30; // number of longitudes/latitudes  
  let rad = 1; //radius of cylinder
  let height = 4; //height of the cylinder


function main() {
  var canvas = document.getElementById('canvas');

  var gl = canvas.getContext('webgl2');

  initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE);

  // Set the vertex coordinates and color
  var n = initVertexBuffers(gl);
  
  // Set clear color 
  gl.clearColor(0.0, 0.0, 0.0, 1.0);
  
  // Get the storage location of u_MvpMatrix
  var u_MvpMatrix = gl.getUniformLocation(gl.program, 'u_MvpMatrix');
  
  // Set the eye point and the viewing volume
  var mvpMatrix = new Matrix4();
  mvpMatrix.setPerspective(30, 1, 1, 100);
  mvpMatrix.lookAt(0, 10, 0, 0, 0, 0, 0, 0, 1);

  function update() {
    mvpMatrix.rotate(0.5,0,0,1); // 0.5 degree z-roll
    mvpMatrix.rotate(1, 1, 0, 0); // 1 degree x-roll

    // Pass the model view projection matrix to u_MvpMatrix
    gl.uniformMatrix4fv(u_MvpMatrix, false, mvpMatrix.elements);

    // Clear color buffer
    gl.clear(gl.COLOR_BUFFER_BIT);

    // Draw cylinder using vertex indices, instead of positions
    // n: total number of indices we'll use to draw triangles
    // gl.UNSIGNED_SHORT: data type of index
    // total number of indices could be over 256 (thus SHORT instead of BYTE)
    // 0: starting offset in bytes 
    gl.drawElements(gl.LINE_LOOP, n, gl.UNSIGNED_SHORT, 0); 

    requestAnimationFrame(update);
  }
  update();
}

//calculates the angle positions for the vertices
function getAngles(){
  let vertices = [];
  for(let i = 0; i < RES; ++i){
      let theta =i * (2 * Math.PI) / RES; //angle for section to be drawn
      let sinTheta = Math.sin(theta); //y angle
      let cosTheta = Math.cos(theta); //x angle

      vertices.push(cosTheta);
      vertices.push(sinTheta);
  }
  
  return vertices;
}


//creates vertices for top and bottom circles
function getCircleVertices(){
  let vertices = [];
      
    

  for(let i = 0; i < 2; ++i){
    let h = -height / 2 + (i * height); //alternates height coordinate
    //center point;
    vertices.push(0);
    vertices.push(0);
    vertices.push(h);
  }

  return vertices;
}


function getSideVertices(){
  let vertices = [];
  let angles = getAngles();
  for(let i = 0; i < 2; ++i){
    let h = -height / 2 + (i * height); //alternates height coordinate
    let count = 0;  //iterator for circle vertices

    for(let j = 0; j <= RES; ++j){
      let cx = angles[count];
      let cy = angles[count + 1];
      let x = cx * rad;
      let y = cy * rad;
      let z = h;

      vertices.push(x);
      vertices.push(y);
      vertices.push(z);
      
      if((count + 2) == angles.length){
        count = 0;
      }
      else{
        count += 2;
      }
      
    }
    
  }
  return vertices;
}

//sets the indicies to be used for drawing the sides
function getSideIndicies(){
  let base = 0;
  let top = RES + 1;
  let inds = [];

  for(let i = 0; i < RES; ++i){

    //first half of side
    inds.push(base);
    inds.push(base + 1);
    inds.push(top);

    //second half of side;
    inds.push(top);
    inds.push(top + 1);
    inds.push(base + 1);
    
    base++;
    top++;
  }
  return inds;
  
}

//sets the indicies to be used for the base circle
function getBaseCircleIndicies(baseCircleIndex){
  let inds = [];
  let offset = RES;
  for(let i = 0; i < RES; ++i){

    if(offset > 0){
      inds.push(offset);
      inds.push(baseCircleIndex);
      inds.push(offset - 1);
      offset--;
    }
    else{
      inds.push(offset);
      inds.push(baseCircleIndex);
      inds.push(RES);
    }
    
  }
  return inds;
}

//sets the indicies to be used for the top circle
function getTopCircleIndicies(topCircleIndex){
  let inds = [];
  let offset = RES + 1;
  for(let i = 0; i < RES; ++i){

    if(offset < RES * 2){
      inds.push(offset);
      inds.push(topCircleIndex);
      inds.push(offset + 1);
      offset++;
    }
    else{
      inds.push(offset);
      inds.push(topCircleIndex);
      inds.push(RES + 1);
    }
    
  }
  return inds;
}

function initVertexBuffers(gl) {

  let vertices = [];
  let indices = [];

  //setup vertices
  vertices = getSideVertices().concat(getCircleVertices());
  let baseIndex = (vertices.length / 3) - 2; //indice location of base center point
  let topIndex = (vertices.length / 3) - 1;  //indice location of top center point
  let sideIndices = getSideIndicies();
  let baseIndices = getBaseCircleIndicies(baseIndex);
  let topIndices = getTopCircleIndicies(topIndex);
  indices = sideIndices.concat(baseIndices, topIndices);
 

 

  vertexData = new Float32Array(vertices);
  indexData = new Uint16Array(indices); // indices may be more than 256
    
  // Write the vertex coordinates and color to the buffer object
  gl.bindBuffer(gl.ARRAY_BUFFER, gl.createBuffer());
  gl.bufferData(gl.ARRAY_BUFFER, vertexData, gl.STATIC_DRAW);
  
  // Assign the buffer object to a_Position and enable the assignment
  var a_Position = gl.getAttribLocation(gl.program, 'a_Position');
  gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
  gl.enableVertexAttribArray(a_Position);
  
  // Write the indices to the buffer object
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, gl.createBuffer());
  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, indexData, gl.STATIC_DRAW);

  return indices.length;
}

