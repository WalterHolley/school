/*
hw2.js
Walter Holley III
CMPSCI4410
Primary js file for polygon generation, placement, scaling, and rotation.
*/

var VSHADER_SOURCE =`#version 300 es
   in vec4 a_Position;
   void main() {     
     gl_Position = a_Position;
}`;

var FSHADER_SOURCE =`#version 300 es
   precision mediump float;
   uniform vec4 u_Color;
   out vec4 cg_FragColor;
   void main() {
     cg_FragColor = u_Color;     
}`;

//polygon properties array
let polygons = [];

function main(){
    var canvas = document.getElementById("canvas");
    var gl = canvas.getContext("webgl2");

    // Initialize shaders
    initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE);

    initVertexBuffers(gl);

    draw(gl);
}

//Polygon properties object
function Polygon(){
    this.vertices = 0;
    this.color = [0,0,0];
    this.center = [0,0];
    this.offset = 0;
}

//returns randomized Polygon attributes
//returns the Polygon function as an object
function randomPolygon(){
    let ranGon = new Polygon();

    //vertices between 3 - 9(inclusive)
    ranGon.vertices = Math.round(Math.random() * (9 - 3) + 3);
    
    ranGon.color = [Math.random(), Math.random(), Math.random()];
    
    let centerX = Math.random() * 2 - 1;
    let centerY = Math.random() * 2 - 1;
    ranGon.center = [centerX, centerY];

    return ranGon;
}

//Draws the polygon collection to the screen
function draw(gl){
    let a_Position = gl.getAttribLocation(gl.program, 'a_Position'); 
    let u_Color = gl.getUniformLocation(gl.program, 'u_Color');  
    const FSIZE = Float32Array.BYTES_PER_ELEMENT; // 4 bytes per float

    //set background and clear canvas
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.clear(gl.COLOR_BUFFER_BIT);

    //set shape vertex attributes and color properties
    for(i = 0; i < polygons.length; i++){
        gl.vertexAttribPointer(a_Position, 2, gl.FLOAT, false, 0, 
                            FSIZE*2*polygons[i].offset);
        gl.enableVertexAttribArray(a_Position);

        //set color
        let color = polygons[i].color;
        gl.uniform4f(u_Color, color[0], color[1], color[2], 1.0);
        
        gl.drawArrays(gl.TRIANGLE_FAN, 0, polygons[i].vertices);
    }
}

//populate the vertice buffers
function initVertexBuffers(gl){
    let points = [];
    let shapeCount = 20;

    //set vertice information for each polygone
    for(i = 0; i < shapeCount; i++){
        polygons.push(randomPolygon());

        let angle = 360 / polygons[i].vertices;
        //radian conversion
        angle = (Math.PI * angle) / 180.0;
        let radius = 0.15;

        let centerX = polygons[i].center[0];
        let centerY = polygons[i].center[1];

        //center position
        points.push(centerX);
        points.push(centerY); 

        // x-y positions for individual vertices
        for(k = 0; k < polygons[i].vertices; k++){
            let x = centerX + Math.cos(angle * k) * radius;
            let y = centerY + Math.sin(angle * k) * radius;
            points.push(x);
            points.push(y);
        }

        //final for end of polygon
        points.push(centerX + radius);
        points.push(centerY);

        //update count for the new vertices
        polygons[i].vertices = polygons[i].vertices + 2;

        if(i > 0){
            polygons[i].offset = polygons[i - 1].offset + polygons[i - 1].vertices;
        }
        
    }

    let vertices = new Float32Array(points);
    var vertexBuffer = gl.createBuffer();

    //bind to target
      gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
    //write data to buffer
    gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
}