#lang racket

(define (my_delete atm list1)
    (cond
        [(null? atm) list1]
        [(null? list1) '()]
        [(list? (car list1)) (cons (my_delete atm (car list1)) (my_delete atm (cdr list1)))]
        [(not (equal? atm (car list1))) (cons (car list1) (my_delete atm (cdr list1)))]
        [else cons car list1 (my_delete atm (cdr list1))]
    )
)

(define testList1 (list 1 2 3 4 5 1 6 2))
(define testList2 (list 3 2 1 (list 3 2 (list 2 5 6 1)) 2 6 5 4))

(my_delete 1 testList1) ;should return (2 3 4 5 6 2)

(my_delete 2 testList2) ;should return (3 1 (3(5 6 1)) 6 5 4)
