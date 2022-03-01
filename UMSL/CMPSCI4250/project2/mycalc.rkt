#lang racket
(define pi 3.1416)

(define (my_calc calc radius)
    (if(= 1 calc)
        (* pi (* radius radius))
        (* (/ 4 3) (* pi (expt radius 3)))
    )
)

(define (my_calc_cond calc radius)
    (cond
        [(null? calc) null]
        [(= calc 1) (* pi (* radius radius))]
        [(= calc 2) (* (/ 4 3) (* pi (expt radius 3)))]
        [(> calc 2) null]
    )
)

(my_calc 1 23) ;should return roughly 1661.9
(my_calc 2 23) ;should return roughly 50965.01
(my_calc_cond 3 23) ;should return null
(my_calc_cond 1 23) ;should return roughly 1661.9
(my_calc_cond 2 23) ;should return roughly 50965.01
(my_calc_cond null 23) ;should return null