#lang racket
(define pi 3.1416)

;part a multiple path implementation
(define (my_calc_a calc radius)
    (cond
        [(or (null? calc) (< radius 0) (<= calc 0) (> calc 2)) #f]
        [(= calc 1) (* pi (* radius radius))]
        [(= calc 2) (* (/ 4 3) (* pi (expt radius 3)))]
    )
)

; part b two-way implementation
(define (my_calc_b calc radius)
    (if (> radius 0)
        (if (null? calc) #f
           (if(= 1 calc)
                (* pi (* radius radius))
                (if(= 2 calc)
                    (* (/ 4 3) (* pi (expt radius 3)))
                    #f
                )
            ) 
        )
        #f
    )
)



(my_calc_a 1 23) ;should return roughly 1661.9
(my_calc_a 2 23) ;should return roughly 50965.01
(my_calc_a 3 23) ;should return #f
(my_calc_a 2 -23); should return #f
(my_calc_a null 23) ;should return #f
(my_calc_b 3 23) ;should return #f
(my_calc_b 1 23) ;should return roughly 1661.9
(my_calc_b 2 23) ;should return roughly 50965.01
(my_calc_b null 23) ;should return #f
(my_calc_b 2 -23) ; should return #f