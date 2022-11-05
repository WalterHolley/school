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
    cg_FragColor = vec4(1.0, 0.6, 0.0, 1.0);
  }
`;

  const RES = 5; // number of longitudes/latitudes  
  let rad = 1; //radius of cylinder
  let height = 2; //height of the cylinder


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
    mvpMatrix.rotate(0.5, 0, 1, 0); // 0.5 degree y-roll
    mvpMatrix.rotate(1, 1, 0, 0); // 1 degree x-roll

    // Pass the model view projection matrix to u_MvpMatrix
    gl.uniformMatrix4fv(u_MvpMatrix, false, mvpMatrix.elements);

    // Clear color buffer
    gl.clear(gl.COLOR_BUFFER_BIT);

    // Draw polyhedron using vertex indices, instead of positions
    // n: total number of indices we'll use to draw triangles
    // gl.UNSIGNED_SHORT: data type of index
    // total number of indices could be over 256 (thus SHORT instead of BYTE)
    // 0: starting offset in bytes 
    gl.drawElements(gl.LINE_LOOP, n, gl.UNSIGNED_SHORT, 0); 

    requestAnimationFrame(update);
  }
  update();
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
    for(let j = 0; j < RES; ++j){
      let theta = j * (2 * Math.PI) / RES; //angle for section to be drawn
      let sinTheta = Math.sin(theta); //y angle
      let cosTheta = Math.cos(theta); //x angle

      vertices.push(cosTheta * rad);
      vertices.push(sinTheta * rad);
      vertices.push(h);
    }
  }

  return vertices;
}

function getSideVertices(){
  let vertices = [];
  let circleVertices = getCircleVertices();
  for(let i = 0; i < 2; ++i){
    let h = -height / 2 + (i * height); //alternates height coordinate
    let count = 0;  //iterator for circle vertices

    for(let j = 0; j <= RES; ++j){
      let cx = circleVertices[count];
      let cy = circleVertices[count + 1];
      let x = cx * rad;
      let y = cy * rad;
      let z = h;

      vertices.push(x);
      vertices.push(y);
      vertices.push(z);
      

      count += 3;
    }
    
  }
  return vertices;
}

function getSideIndicies(){
  let indicies = [];
  
}

function getBaseCircleIndicies(baseCircleIndex){
  let inds = [];
  let offset = baseCircleIndex + 1;
  for(let i = 0; i < RES; ++i){

    if(i < RES - 1){
      inds.push(baseCircleIndex);
      inds.push(offset + 1);
      inds.push(offset);
      offset++;
    }
    else{
      inds.push(baseCircleIndex);
      inds.push(baseCircleIndex + 1);
      inds.push(offset);
    }
    
  }
  return inds;
}

function getTopCircleIndicies(topCircleIndex){
  let inds = [];
  let offset = topCircleIndex + 1;
  for(let i = 0; i < RES; ++i){

    if(i < RES - 1){
      inds.push(topCircleIndex);
      inds.push(offset);
      inds.push(offset + 1);
      offset++;
    }
    else{
      inds.push(topCircleIndex);
      inds.push(offset);
      inds.push(topCircleIndex + 1);
    }
    
  }
  return inds;
}

function initVertexBuffers(gl) {

  let vertices = [];
  let indices = [];

  //setup vertices
  vertices = getCircleVertices();
  //vertices.concat(getSideVertices());
  baseIndex = 0;
  topIndex = RES + 1;
  indices = getBaseCircleIndicies(baseIndex).concat(getTopCircleIndicies(topIndex));
 

 

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

