#lang racket

(define (my_delete atm list1)(
    (cond
        [(null? atm) list1]
        [(null? list1) list1]
        [else cons(remove (list atm) cdr(list1))]
    )
))

(my_delete 1 (list 1 2 3 4 5 6 2)) ;should return (2 3 4 5 6 2)
(my_delete '2 (list 1 2 3 4 5 6 2)) ;should return (1 3 4 5 6)