#lang racket
;returns the union of two lists
;duplicates will not be repeated in the list
(provide my_union)

(define (my_union list1 list2)
    (remove-duplicates (append* list1 list2)) ;refactor this to use code from memebership.rkt
)