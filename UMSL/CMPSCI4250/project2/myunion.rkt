#lang racket

(define (membership atm a_list)
  (cond
       ((null? a_list) #f)
       ((eq? atm (car a_list)) #t)
       (else (membership atm (cdr a_list)))
 ))


(define (my_union list1 list2)(
    (cond 
        [(null? list1) list2]
        [(null? list2) list1]
        [(membership (car list1) list2)
            (my_union (cdr list1) list2)]
        [else (my_union(cons(car(list1) list2)(cdr list2)))]
        
    )
))
