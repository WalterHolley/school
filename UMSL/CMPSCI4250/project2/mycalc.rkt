#lang racket
(define pi 3.1416)

(define (my_calc calc radius)(
    (if(= 1 calc)
        (* pi (* radius radius))
        ((* (/ 4 3) (* pi (expt radius 3)))))
))

(define (my_calc_cond calc radius)
((cond
    [(= calc 1) (* pi (* radius radius))]
    [(= calc 2) (* (/ 4 3) (* pi (expt radius 3)))]
    ))
)