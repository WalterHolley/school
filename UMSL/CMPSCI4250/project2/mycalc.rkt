#lang racket
(provide my_calc)
(provide pi)
(define pi 3.1416)  ;define a constant for pi

;function to calculate 2D area of circle
;or volume of sphere
(define (my_calc operation radius) 
    (if (= 1 operation)
        (* (expt radius 2) pi)
        (* (/ 4 3) (* pi (expt radius 3)))
    )
)