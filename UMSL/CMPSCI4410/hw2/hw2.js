/*
hw2.js
Walter Holley III
CMPSCI4410
Primary js file for polygon generation, placement, scaling, and rotation.
*/

var VSHADER_SOURCE =`#version 300 es
   in vec4 a_Position;
   uniform mat4 u_modelMatrix;
   void main() {     
     gl_Position = u_modelMatrix * a_Position;
}`;

var FSHADER_SOURCE =`#version 300 es
   precision mediump float;
   uniform vec4 u_Color;
   out vec4 cg_FragColor;
   void main() {
     cg_FragColor = u_Color;     
}`;

//Rotation angle
let ROTATION_ANGLE = 1.0;
let ROTATION_SPEED = 1.0;
 let shapeCount = 20; //number of polygons to draw
//polygon properties array
let polygons = [];
let currentAngle = 0.0;
let radius = 0.15;

function main(){
    var canvas = document.getElementById("canvas");
    var gl = canvas.getContext("webgl2");

    // Initialize shaders
   if(!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)){
      console.log("There was a problem initializing the shader program");
      return;
   }
    

    initVertexBuffers(gl);

    //get shader rotation Matrix4
    var u_modelMatrix = gl.getUniformLocation(gl.program, 'u_modelMatrix');
    if(!u_modelMatrix){
       console.log("There was a problem with webgl2");
       return;
    }

    //controls the timing for the animation
    var timeTick = function(){
       //update angle for rotation
       draw(gl, u_modelMatrix);
       requestAnimationFrame(timeTick, canvas);
    }

    timeTick();
    
}

// Last time that this function was called
var g_last = Date.now();
function animate(angle) {
  // Calculate the elapsed time
  var now = Date.now();
  var elapsed = (now - g_last) *  0.0001;
  g_last = now;
  // Update the current rotation angle (adjusted by the elapsed time)
  var newAngle = angle + (ROTATION_ANGLE * elapsed) * ROTATION_SPEED;
  return newAngle %= 360;
}

//Polygon properties object
function Polygon(){
    this.vertices = 0;
    this.color = [0,0,0];
    this.center = [0,0];
    this.offset = 0;
    this.angle = 0;
    this.matrix = 0;
    this.scale = 1.0;
    this.scaleUp = false;
    this.scaleRate = 0.0;
}

//returns randomized Polygon attributes
//returns the Polygon function as an object
function randomPolygon(){
    let ranGon = new Polygon();

    ranGon.matrix = new Matrix4();
    //vertices between 3 - 9(inclusive)
    ranGon.vertices = Math.round(Math.random() * (9 - 3) + 3);

    //init angle
    ranGon.angle = 360 / ranGon.vertices;

    //initial scale
    ranGon.scale = Math.round(Math.random() * 1);
    if(Math.random() >= 0.5){
       ranGon.scaleUp = true;
    }
    else{
       ranGon.scaleUp = false;
    }

    ranGon.scaleRate = Math.random() * 0.005;

    
    ranGon.color = [Math.random(), Math.random(), Math.random()];
    
    let centerX = Math.random() * 2 - 1;
    let centerY = Math.random() * 2 - 1;
    ranGon.center = [centerX, centerY];

    return ranGon;
}

//Draws the polygon collection to the screen
function draw(gl, shaderModelMatrix){
    let a_Position = gl.getAttribLocation(gl.program, 'a_Position'); 
    let u_Color = gl.getUniformLocation(gl.program, 'u_Color');  
    const FSIZE = Float32Array.BYTES_PER_ELEMENT; // 4 bytes per float

    //set background and clear canvas
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.clear(gl.COLOR_BUFFER_BIT);

   


    //update vertices positions
    //let vertices = updateVertices();

    //write new vertice data to vertex buffer
    //updateVertexBuffer(vertices, gl);

    //set shape vertex attributes and color properties
    for(let i = 0; i < polygons.length; i++){

        gl.vertexAttribPointer(a_Position, 2, gl.FLOAT, false, 0, 
                            FSIZE*2*polygons[i].offset);
        gl.enableVertexAttribArray(a_Position);
        



         //update scaling
         if(polygons[i].scaleUp){
            polygons[i].scale += polygons[i].scaleRate;
            if(polygons[i].scale >= 1.0){
               polygons[i].scaleUp = false;
            }
         }
          else{
             polygons[i].scale -= polygons[i].scaleRate;
             if(polygons[i].scale <= 0){
                polygons[i].scaleUp = true;
             }
          }
       
       let scale = polygons[i].scale;
       let centerX = polygons[i].center[0];
       let centerY = polygons[i].center[1];

        //update angle
        polygons[i].angle = ((polygons[i].angle + 1) % 360);
         //update radians
         let radians = (Math.PI * polygons[i].angle) / 180.0;


      

       //compute position matrix
       polygons[i].matrix.setTranslate(centerX, centerY, 1);
       
       polygons[i].matrix.rotate(polygons[i].angle,0,0,1);
       polygons[i].matrix.scale(polygons[i].scale,polygons[i].scale,1);
       //polygons[i].matrix.translate(centerX, centerY, 1);
        
       //set color
       let color = polygons[i].color;
       gl.uniform4f(u_Color, color[0], color[1], color[2], 1.0);

       //set modelMatrix
       gl.uniformMatrix4fv(shaderModelMatrix, false, polygons[i].matrix.elements);
 
       gl.drawArrays(gl.TRIANGLE_FAN, 0, polygons[i].vertices);

    }
}

//initializes the vertice buffer.  Run once
function initVertexBuffers(gl){
   
  

   //set vertice information for each polygon
   for(i = 0; i < shapeCount; i++){
       polygons.push(randomPolygon());

       //additional vert count for center and final vertices
       polygons[i].vertices += 2;

       if(i > 0){
           polygons[i].offset = polygons[i - 1].offset + polygons[i - 1].vertices;
       }       
   }

   let vertices = updateVertices();
   updateVertexBuffer(vertices, gl);

}

//Creates a Float32Array of vertice information
function updateVertices(){
   let points = [];
   

   for(i = 0; i < polygons.length; i++){

      //radian conversion  
      let angle = (Math.PI * polygons[i].angle) / 180.0;
    

      let centerX = polygons[i].center[0];
      let centerY = polygons[i].center[1];

      //center position
      points.push(centerX);
      points.push(centerY); 

      // x-y positions for shape vertices
      for(k = 0; k < polygons[i].vertices - 2; k++){
         let x = centerX + Math.cos(angle * k) * radius;
         let y = centerY + Math.sin(angle * k) * radius;
         points.push(x);
         points.push(y);
      }

      //final for end of polygon
      points.push(centerX + radius);
      points.push(centerY);  
   }

   return new Float32Array(points);
   
   
}

//updates vertex buffer information
function updateVertexBuffer(vertices, gl){
   var vertexBuffer = gl.createBuffer();

   //bind to target
   gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
   //write data to buffer
   gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
   
}