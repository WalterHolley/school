#lang racket

(define (membership atm a_list)
  (cond
       ((null? a_list) #f)
       ((eq? atm (car a_list)) #t)
       (else (membership atm (cdr a_list)))
 ))


(define (my_union list1 list2)
    (cond 
        [(null? list2) list1]
        [(membership (car list2) list1) (my_union list1 (cdr list2))]
        [else (my_union (cons (car list2) list1) (cdr list2))]      
    )
)

(define testList1 (list 1 2 3 4 5))
(define testList2 (list 5 6 7 5 8 9))

(my_union testList1 testList2) ;should return (9 8 7 6 1 2 3 4 5)
(my_union testList1 testList1) ;should return (1 2 3 4 5)
