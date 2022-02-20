#lang racket
;takes in a list, and removes the second element
;from the list.  If there are less than two
;elements in the list, nothing is returned
(provide rem_second)

(define (rem_second items)
    (if (>= 2 (length items))
        (remove (second items) items)
        (list )
    )
)