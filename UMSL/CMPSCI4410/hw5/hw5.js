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

function initVertexBuffers(gl) {
  const RES = 20; // number of longitudes/latitudes  
  let rad = 1; //radius of cylinder
  let height = 2; //height of the cylinder

  let vertices = [];
  let indices = [];

  //setup vertices
  for(let i = 0; i <= RES; i++){
    let theta = i * (2 * Math.PI) / RES; //angle for section to be drawn
    let sinTheta = Math.sin(theta);
    let cosTheta = Math.cos(theta);

    let x = rad * cosTheta;
    let y = rad * sinTheta;
    let z = 0;
    

    vertices.push(x);
    vertices.push(y);
    vertices.push(z);
    
    
  }

  
  let center = vertices.length / 3;
  //setup indicies
  for(let i = 0; i <= RES; ++i){
    
    let left = (i + 1) % (RES + 1);
    let right = (i + 2) % (RES + 1);

    indices.push(left);
    indices.push(right);
    indices.push(center);
    
  }
 

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

