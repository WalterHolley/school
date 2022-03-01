#lang racket

(define (rem_second list1)
    (cond
        [(null? list1) null]
        [(= 2 (length list1)) (list (car list1))]
        [(> (length list1) 2) (cons (car list1) (cddr list1))]
        [else null]
    )
)

(define testList1 (list 1 2 3 4 5 6 7))
(define testList2 (list 1 2))
(define testList3 null)
(define testList4 (list 1))

(rem_second testList1) ;should return (1 3 4 5 6 7)
(rem_second testList2) ;should return (1)
(rem_second testList3) ;should return ()
(rem_second testList4) ;should return ()