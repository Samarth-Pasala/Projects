;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |Samarth and Jacob - Homework 9 (2) (1)|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;;Project: FunPaint

;;Task 1

(require 2htdp/universe)
(require 2htdp/image)

(define-struct draw-circ [radius color set])

;; A draw-circ is a (make-draw-circ Number String String)
#|
Interpretation: a (make-draw-circ radius color set) represents:
- radius is the radius of the circle
- color is the color of the circle
- set is whether the circle is solid or not
|#

;; Examples
(define DRAW-CIRC1 (make-draw-circ 10 "blue" "solid"))
(define DRAW-CIRC2 (make-draw-circ 15 "green" "solid"))
(define DRAW-CIRC3 (make-draw-circ 20 "orange" "solid"))

;; draw-circ-templ -> ?
#;
(define (draw-circ-templ circ)
  (... (draw-circ-radius circ) ...
       (draw-circ color circ) ...
       (draw-circ-set circ) ...))

(define-struct draw-tri [size color set])

;; A draw-tri is a (make-draw-tri Number String String)
#|
Interpretation: a (make-draw-tri size color set) represents:
- size is the size of the triangle
- color is the color of the triangle
- set is whether the triangle is solid or not
|#

;; Examples
(define DRAW-TRI1 (make-draw-tri 10 "blue" "solid"))
(define DRAW-TRI2 (make-draw-tri 15 "green" "solid"))
(define DRAW-TRI3 (make-draw-tri 20 "orange" "solid"))

;; draw-tri-templ -> ?
#;
(define (draw-tri-templ tri)
  (... (draw-tri-size tri) ...
       (draw-tri-color tri) ...
       (draw-tri-set tri) ...))

(define-struct draw-rect [height width color set])

;; A draw-rect is a (make-draw-rect Number Number String String)
#|
Interpretation: a (make-draw-recr height width color set) represents:
- height represents the height of the rectangle
- width represents the width of the rectangle
- color represents the color of the rectangle
- set represents whether the rectangle is solid or not
|#

;; Examples
(define DRAW-RECT1 (make-draw-rect 10 10 "blue" "solid"))
(define DRAW-RECT2 (make-draw-rect 15 15 "green" "solid"))
(define DRAW-RECT3 (make-draw-rect 20 70 "orange" "solid"))

;; draw-rect-temp -> ?
#;
(define (draw-rect-templ rect)
  (... (draw-rect-height rect) ...
       (draw-rect-width rect) ...
       (draw-rect-color rect) ...
       (draw-rect-set rect) ...))

(define-struct draw-sta [size color set])

;; A draw-sta is a (make-draw-sta Number String String)
#|
Interpretation: a (make-draw-sta size color set) represents:
- size represents the size of the star
- color represents the color of the star
- set represents whether the star is solid or not
|#

;; Examples
(define DRAW-STA1 (make-draw-sta 10 "blue" "solid"))
(define DRAW-STA2 (make-draw-sta 15 "green" "solid"))
(define DRAW-STA3 (make-draw-sta 20 "orange" "solid"))

;; draw-sta-templ -> ?
#;
(define (draw-sta-templ sta)
  (... (draw-sta-size sta) ...
       (draw-sta-color sta) ...
       (draw-sta-set sta) ...))

#|
A Shape is one of:
- draw-circ
- draw-tri
- draw-rect
- draw-sta
|#

;; Interpretation: Represents the current shape in the world state

;; Examples
(define SHAPE1 DRAW-CIRC1)
(define SHAPE2 DRAW-TRI2)
(define SHAPE3 DRAW-RECT3)
(define SHAPE4 DRAW-STA3)

;; shape-templ : Shape -> ?
#;
(define (shape-templ shape)
  (cond
    [(draw-circ? shape) (... (draw-circ-templ shape) ...)]
    [(draw-tri? shape) (... (draw-tri-templ shape) ...)]
    [(draw-rect? shape) (... (draw-rect-templ shape) ...)]
    [(draw-sta? shape) (... (draw-sta-templ shape) ...)]))

;; Task 2

(define BACKGROUND (empty-scene 500 500))

(define-struct placed-shape [shape posn background])

;; A placed-shape is a (make-placed-shape Shape make-posn Image)

#|
Interpretation: A (make-placed-shape [shape posn background]) represents:
- shape represents the shape that will be placed
- posn represents the coordinates at which the shape will be placed
- background represents the canvas the shape will be placed on
|#

;; Examples
(define PLACED-SHAPE1 (make-placed-shape SHAPE1 (make-posn 50 50) BACKGROUND))
(define PLACED-SHAPE2 (make-placed-shape SHAPE2 (make-posn 100 100) BACKGROUND))
(define PLACED-SHAPE3 (make-placed-shape SHAPE3 (make-posn 150 150) BACKGROUND))
(define PLACED-SHAPE4 (make-placed-shape SHAPE4 (make-posn 300 300) BACKGROUND))

;; placed-shape-templ : placed-shape -> ?
#;
(define (placed-shape-templ p)
  (... (shape-templ (placed-shape-shape p)) ...
       (posn-x (placed-shape-posn p)) ...
       (posn-y (placed-shape-posn p)) ...
       (placed-shape-background p) ...))

;; FunPaint Part 2

;; Task 1

;; ps : placed-shape -> placed-shape
;; purpose statement: Outputs shape onto an empty scene

(define (ps p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-placed-shape
      (placed-shape-shape p)
      (placed-shape-posn p)
      (place-image (circle
                    (draw-circ-radius (placed-shape-shape p))
                    "solid"
                    (draw-circ-color (placed-shape-shape p)))
                   (posn-x (placed-shape-posn p))
                   (posn-y (placed-shape-posn p))
                   (placed-shape-background p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-placed-shape
      (placed-shape-shape p)
      (placed-shape-posn p)
      (place-image (triangle
                    (draw-tri-size (placed-shape-shape p))
                    "solid"
                    (draw-tri-color (placed-shape-shape p)))
                   (posn-x (placed-shape-posn p))
                   (posn-y (placed-shape-posn p))
                   (placed-shape-background p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-placed-shape
      (placed-shape-shape p)
      (placed-shape-posn p)
      (place-image (rectangle
                    (draw-rect-width (placed-shape-shape p))
                    (draw-rect-height (placed-shape-shape p))
                    "solid"
                    (draw-rect-color (placed-shape-shape p)))
                   (posn-x (placed-shape-posn p))
                   (posn-y (placed-shape-posn p))
                   (placed-shape-background p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-placed-shape
      (placed-shape-shape p)
      (placed-shape-posn p)
      (place-image (star
                    (draw-sta-size (placed-shape-shape p))
                    "solid"
                    (draw-sta-color (placed-shape-shape p)))
                   (posn-x (placed-shape-posn p))
                   (posn-y (placed-shape-posn p))
                   (placed-shape-background p)))]))


(check-expect (ps PLACED-SHAPE1)
              (make-placed-shape (make-draw-circ 10 "blue" "solid") (make-posn 50 50)
                                 (place-image (circle
                                               (draw-circ-radius (placed-shape-shape PLACED-SHAPE1))
                                               "solid"
                                               (draw-circ-color (placed-shape-shape PLACED-SHAPE1)))
                                              (posn-x (placed-shape-posn PLACED-SHAPE1))
                                              (posn-y (placed-shape-posn PLACED-SHAPE1))
                                              (placed-shape-background PLACED-SHAPE1))))
(check-expect (ps PLACED-SHAPE2)
              (make-placed-shape (make-draw-tri 15 "green" "solid") (make-posn 100 100)
                                 (place-image (triangle
                                               (draw-tri-size (placed-shape-shape PLACED-SHAPE2))
                                               "solid"
                                               (draw-tri-color (placed-shape-shape PLACED-SHAPE2)))
                                              (posn-x (placed-shape-posn PLACED-SHAPE2))
                                              (posn-y (placed-shape-posn PLACED-SHAPE2))
                                              (placed-shape-background PLACED-SHAPE2))))
(check-expect (ps PLACED-SHAPE4)
              (make-placed-shape (make-draw-sta 20 "orange" "solid") (make-posn 300 300)
                                 (place-image (star
                                               (draw-sta-size (placed-shape-shape PLACED-SHAPE4))
                                               "solid"
                                               (draw-sta-color (placed-shape-shape PLACED-SHAPE4)))
                                              (posn-x (placed-shape-posn PLACED-SHAPE4))
                                              (posn-y (placed-shape-posn PLACED-SHAPE4))
                                              (placed-shape-background PLACED-SHAPE4))))

;; handle-mouse : placed-shape Number Number String -> placed-shape
;; purpose statement: Updates position to current mouse position and places image when clicked

(define (handle-mouse p x y mouse)
  (cond
    [(string=? mouse "move")
     (new-posn p x y)]
    [(string=? mouse "button-down")
     ;;Task 5
     (if (< y 30)
         (button-press? p x)
         (ps p))]
    [else (new-posn p x y)]))

(check-expect (handle-mouse PLACED-SHAPE1 30 30 "move")
              (make-placed-shape (make-draw-circ 10 "blue" "solid") (make-posn 30 30) BACKGROUND))
(check-expect (handle-mouse PLACED-SHAPE2 50 50 "button-down")
              (ps PLACED-SHAPE2))
(check-expect (handle-mouse PLACED-SHAPE4 70 70 "button-down")
              (ps PLACED-SHAPE4))



;;Task 2

;; A Function is one of
;; - A Shape
;; - A color in string
;;   - "red" "orange" "yellow" "green" "blue" and "violet"
;; - "+" or "-"

#|
Interpretation: a Function is a either a string or shape that returns what the button outputs
|#

;; Examples
(define FUNCTION-RED "red")
(define FUNCTION-CIRCLE (make-draw-circ 10 "blue" "solid"))
(define FUNCTION-CALC "+")

;; Template : f-templ : Function -> ?
#;
(define (f-templ f)
  (cond
    [(shape? f) (shape-templ f)] ...
    [(string=? f "red")] ...
    [(string=? f "orange")] ...
    [(string=? f "yellow")] ...
    [(string=? f "green")] ...
    [(string=? f "blue")] ...
    [(string=? f "violet")] ...
    [(string=? f "+")] ...
    [(string=? f "-")] ...))


(define-struct button [func pos shape])

;; A button is a (make-button Function Posn Shape)
#|
Interpretation: a (make-button func pos shape) represents:
- func which is what the button respresnts
- pos which is where the button is on the screen
- shape which is the shape of the button
|#

;; Examples
(define CIRC-BUTTON (make-button (make-draw-circ 10 "black" "solid") (make-posn 10 10)
                                 (circle 10 "solid" "black")))
(define TRI-BUTTON (make-button (make-draw-tri 10 "black" "solid") (make-posn 30 10)
                                (triangle 20 "solid" "black")))
(define RECT-BUTTON (make-button (make-draw-rect 5 10 "black" "solid") (make-posn 50 15)
                                 (rectangle 20 10 "solid" "black")))
(define STA-BUTTON (make-button (make-draw-sta 10 "black" "solid") (make-posn 70 10)
                                (star 12 "solid" "black")))
(define RED-BUTTON (make-button "red" (make-posn 90 10) (square 20 "solid" "red")))
(define ORANGE-BUTTON (make-button "orange" (make-posn 110 10) (square 20 "solid" "orange")))
(define YELLOW-BUTTON (make-button "yellow" (make-posn 130 10) (square 20 "solid" "yellow")))
(define GREEN-BUTTON (make-button "green" (make-posn 150 10) (square 20 "solid" "green")))
(define BLUE-BUTTON (make-button "blue" (make-posn 170 10) (square 20 "solid" "blue")))
(define VIOLET-BUTTON (make-button "violet" (make-posn 190 10) (square 20 "solid" "violet")))
(define PLUS-BUTTON (make-button "+" (make-posn 210 10) (underlay (rectangle 20 5 "solid" "black")
                                                                  (rectangle 5 20 "solid" "black"))))
(define MINUS-BUTTON (make-button "-" (make-posn 230 15) (rectangle 20 10 "solid" "black")))

;; Template : button-templ : Button -> ?
#;
(define (button-templ b)
  (... (f-templ (button-func b)) ...)
  (... (button-pos b) ...)
  (... (shape-templ (button-shape b)) ...))

;;Task 3

;; sha : placed-shape -> Image
;; purpose statement: makes a shape based on the color and shape type of the placed-shape

(define (sha p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (circle 15 "solid" (draw-circ-color (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (triangle 30 "solid" (draw-tri-color (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (rectangle 30 20 "solid" (draw-rect-color (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (star 30 "solid" (draw-sta-color (placed-shape-shape p)))]))

(check-expect (sha PLACED-SHAPE1) (circle 15 "solid" "blue"))
(check-expect (sha PLACED-SHAPE2) (triangle 30 "solid" "green"))
(check-expect (sha PLACED-SHAPE3) (rectangle 30 20 "solid" "orange"))

;; get-shape-property : placed-shape -> Number
;; purpose statement: gets the size of the placed-shape

(define (get-shape-property p)
  (cond [(draw-circ? (placed-shape-shape p))
         (* 2 (draw-circ-radius (placed-shape-shape p)))]
        [(draw-tri? (placed-shape-shape p))
         (draw-tri-size (placed-shape-shape p))]
        [(draw-rect? (placed-shape-shape p))
         (draw-rect-width (placed-shape-shape p))]
        [(draw-sta? (placed-shape-shape p))
         (draw-sta-size (placed-shape-shape p))]))

(check-expect (get-shape-property PLACED-SHAPE1) 20)
(check-expect (get-shape-property PLACED-SHAPE2) 15)
(check-expect (get-shape-property PLACED-SHAPE3) 70)

;; draw-world : placed-shape -> Image
;; purpose statement: makes the toolbar and places the images saved on the placed-shape

(define (draw-world p)
  (let ((shape-property (get-shape-property p)))
    (underlay/xy
     (underlay/xy
      (underlay/xy
       (underlay/xy
        (underlay/xy
         (underlay/xy
          (underlay/xy
           (underlay/xy
            (underlay/xy
             (underlay/xy
              (underlay/xy
               (underlay/xy
                (underlay/xy
                 (underlay/xy (placed-shape-background p)
                              (posn-x (button-pos CIRC-BUTTON)) (posn-y (button-pos CIRC-BUTTON))
                              (button-shape CIRC-BUTTON))
                 (posn-x (button-pos TRI-BUTTON)) (posn-y (button-pos TRI-BUTTON))
                 (button-shape TRI-BUTTON))
                (posn-x (button-pos RECT-BUTTON)) (posn-y (button-pos RECT-BUTTON))
                (button-shape RECT-BUTTON))
               (posn-x (button-pos STA-BUTTON)) (posn-y (button-pos STA-BUTTON))
               (button-shape STA-BUTTON))
              (posn-x (button-pos RED-BUTTON)) (posn-y (button-pos RED-BUTTON))
              (button-shape RED-BUTTON))
             (posn-x (button-pos ORANGE-BUTTON)) (posn-y (button-pos ORANGE-BUTTON))
             (button-shape ORANGE-BUTTON))
            (posn-x (button-pos YELLOW-BUTTON)) (posn-y (button-pos YELLOW-BUTTON))
            (button-shape YELLOW-BUTTON))
           (posn-x (button-pos GREEN-BUTTON)) (posn-y (button-pos GREEN-BUTTON))
           (button-shape GREEN-BUTTON))
          (posn-x (button-pos BLUE-BUTTON)) (posn-y (button-pos BLUE-BUTTON))
          (button-shape BLUE-BUTTON))
         (posn-x (button-pos VIOLET-BUTTON)) (posn-y (button-pos VIOLET-BUTTON))
         (button-shape VIOLET-BUTTON))
        (posn-x (button-pos PLUS-BUTTON)) (posn-y (button-pos PLUS-BUTTON))
        (button-shape PLUS-BUTTON))
       (posn-x (button-pos MINUS-BUTTON)) (posn-y (button-pos MINUS-BUTTON))
       (button-shape MINUS-BUTTON))
      10 40 (sha p))
     10 70 (text (number->string shape-property) 20 "black"))))

(check-expect (draw-world PLACED-SHAPE1)
    (underlay/xy
     (underlay/xy
      (underlay/xy
       (underlay/xy
        (underlay/xy
         (underlay/xy
          (underlay/xy
           (underlay/xy
            (underlay/xy
             (underlay/xy
              (underlay/xy
               (underlay/xy
                (underlay/xy
                 (underlay/xy BACKGROUND
                              (posn-x (button-pos CIRC-BUTTON)) (posn-y (button-pos CIRC-BUTTON))
                              (button-shape CIRC-BUTTON))
                 (posn-x (button-pos TRI-BUTTON)) (posn-y (button-pos TRI-BUTTON))
                 (button-shape TRI-BUTTON))
                (posn-x (button-pos RECT-BUTTON)) (posn-y (button-pos RECT-BUTTON))
                (button-shape RECT-BUTTON))
               (posn-x (button-pos STA-BUTTON)) (posn-y (button-pos STA-BUTTON))
               (button-shape STA-BUTTON))
              (posn-x (button-pos RED-BUTTON)) (posn-y (button-pos RED-BUTTON))
              (button-shape RED-BUTTON))
             (posn-x (button-pos ORANGE-BUTTON)) (posn-y (button-pos ORANGE-BUTTON))
             (button-shape ORANGE-BUTTON))
            (posn-x (button-pos YELLOW-BUTTON)) (posn-y (button-pos YELLOW-BUTTON))
            (button-shape YELLOW-BUTTON))
           (posn-x (button-pos GREEN-BUTTON)) (posn-y (button-pos GREEN-BUTTON))
           (button-shape GREEN-BUTTON))
          (posn-x (button-pos BLUE-BUTTON)) (posn-y (button-pos BLUE-BUTTON))
          (button-shape BLUE-BUTTON))
         (posn-x (button-pos VIOLET-BUTTON)) (posn-y (button-pos VIOLET-BUTTON))
         (button-shape VIOLET-BUTTON))
        (posn-x (button-pos PLUS-BUTTON)) (posn-y (button-pos PLUS-BUTTON))
        (button-shape PLUS-BUTTON))
       (posn-x (button-pos MINUS-BUTTON)) (posn-y (button-pos MINUS-BUTTON))
       (button-shape MINUS-BUTTON))
      10 40 (circle 15 "solid" "blue"))
     10 70 (text "20" 20 "black")))
(check-expect (draw-world PLACED-SHAPE2)
    (underlay/xy
     (underlay/xy
      (underlay/xy
       (underlay/xy
        (underlay/xy
         (underlay/xy
          (underlay/xy
           (underlay/xy
            (underlay/xy
             (underlay/xy
              (underlay/xy
               (underlay/xy
                (underlay/xy
                 (underlay/xy BACKGROUND
                              (posn-x (button-pos CIRC-BUTTON)) (posn-y (button-pos CIRC-BUTTON))
                              (button-shape CIRC-BUTTON))
                 (posn-x (button-pos TRI-BUTTON)) (posn-y (button-pos TRI-BUTTON))
                 (button-shape TRI-BUTTON))
                (posn-x (button-pos RECT-BUTTON)) (posn-y (button-pos RECT-BUTTON))
                (button-shape RECT-BUTTON))
               (posn-x (button-pos STA-BUTTON)) (posn-y (button-pos STA-BUTTON))
               (button-shape STA-BUTTON))
              (posn-x (button-pos RED-BUTTON)) (posn-y (button-pos RED-BUTTON))
              (button-shape RED-BUTTON))
             (posn-x (button-pos ORANGE-BUTTON)) (posn-y (button-pos ORANGE-BUTTON))
             (button-shape ORANGE-BUTTON))
            (posn-x (button-pos YELLOW-BUTTON)) (posn-y (button-pos YELLOW-BUTTON))
            (button-shape YELLOW-BUTTON))
           (posn-x (button-pos GREEN-BUTTON)) (posn-y (button-pos GREEN-BUTTON))
           (button-shape GREEN-BUTTON))
          (posn-x (button-pos BLUE-BUTTON)) (posn-y (button-pos BLUE-BUTTON))
          (button-shape BLUE-BUTTON))
         (posn-x (button-pos VIOLET-BUTTON)) (posn-y (button-pos VIOLET-BUTTON))
         (button-shape VIOLET-BUTTON))
        (posn-x (button-pos PLUS-BUTTON)) (posn-y (button-pos PLUS-BUTTON))
        (button-shape PLUS-BUTTON))
       (posn-x (button-pos MINUS-BUTTON)) (posn-y (button-pos MINUS-BUTTON))
       (button-shape MINUS-BUTTON))
      10 40 (triangle 30 "solid" "green"))
     10 70 (text "15" 20 "black")))
(check-expect (draw-world PLACED-SHAPE3)
    (underlay/xy
     (underlay/xy
      (underlay/xy
       (underlay/xy
        (underlay/xy
         (underlay/xy
          (underlay/xy
           (underlay/xy
            (underlay/xy
             (underlay/xy
              (underlay/xy
               (underlay/xy
                (underlay/xy
                 (underlay/xy BACKGROUND
                              (posn-x (button-pos CIRC-BUTTON)) (posn-y (button-pos CIRC-BUTTON))
                              (button-shape CIRC-BUTTON))
                 (posn-x (button-pos TRI-BUTTON)) (posn-y (button-pos TRI-BUTTON))
                 (button-shape TRI-BUTTON))
                (posn-x (button-pos RECT-BUTTON)) (posn-y (button-pos RECT-BUTTON))
                (button-shape RECT-BUTTON))
               (posn-x (button-pos STA-BUTTON)) (posn-y (button-pos STA-BUTTON))
               (button-shape STA-BUTTON))
              (posn-x (button-pos RED-BUTTON)) (posn-y (button-pos RED-BUTTON))
              (button-shape RED-BUTTON))
             (posn-x (button-pos ORANGE-BUTTON)) (posn-y (button-pos ORANGE-BUTTON))
             (button-shape ORANGE-BUTTON))
            (posn-x (button-pos YELLOW-BUTTON)) (posn-y (button-pos YELLOW-BUTTON))
            (button-shape YELLOW-BUTTON))
           (posn-x (button-pos GREEN-BUTTON)) (posn-y (button-pos GREEN-BUTTON))
           (button-shape GREEN-BUTTON))
          (posn-x (button-pos BLUE-BUTTON)) (posn-y (button-pos BLUE-BUTTON))
          (button-shape BLUE-BUTTON))
         (posn-x (button-pos VIOLET-BUTTON)) (posn-y (button-pos VIOLET-BUTTON))
         (button-shape VIOLET-BUTTON))
        (posn-x (button-pos PLUS-BUTTON)) (posn-y (button-pos PLUS-BUTTON))
        (button-shape PLUS-BUTTON))
       (posn-x (button-pos MINUS-BUTTON)) (posn-y (button-pos MINUS-BUTTON))
       (button-shape MINUS-BUTTON))
      10 40 (rectangle 30 20 "solid" "orange"))
     10 70 (text "70" 20 "black")))

;; Task 4

;; new-posn : placed-shaped Number Number -> placed-shape
;; purpose statement: creates shape at the current mouse position

(define (new-posn p x y)
  (make-placed-shape (placed-shape-shape p) (make-posn x y) (placed-shape-background p)))

(check-expect (new-posn PLACED-SHAPE1 30 30)
              (make-placed-shape (make-draw-circ 10 "blue" "solid") (make-posn 30 30) BACKGROUND))
(check-expect (new-posn PLACED-SHAPE2 50 50)
              (make-placed-shape (make-draw-tri 15 "green" "solid") (make-posn 50 50) BACKGROUND))
(check-expect (new-posn PLACED-SHAPE4 70 70)
              (make-placed-shape (make-draw-sta 20 "orange" "solid") (make-posn 70 70) BACKGROUND))

;; shape-to-circ : placed-shape -> placed-shape
;; purpose statement: changers the shape from whatever it is to a circle

(define (shape-to-circ p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-circ (draw-circ-radius (placed-shape-shape p))
                     (draw-circ-color (placed-shape-shape p))
                     (draw-circ-set (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-circ (/ (draw-tri-size (placed-shape-shape p)) 2)
                     (draw-tri-color (placed-shape-shape p))
                     (draw-tri-set (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-circ (/ (draw-rect-width (placed-shape-shape p)) 2)
                     (draw-rect-color (placed-shape-shape p))
                     (draw-rect-set (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-circ (/ (draw-sta-size (placed-shape-shape p)) 2)
                     (draw-sta-color (placed-shape-shape p))
                     (draw-sta-set (placed-shape-shape p)))]))

(check-expect (shape-to-circ PLACED-SHAPE2) (make-draw-circ 7.5 "green" "solid"))
(check-expect (shape-to-circ PLACED-SHAPE3) (make-draw-circ 35 "orange" "solid"))
(check-expect (shape-to-circ PLACED-SHAPE4) (make-draw-circ 10 "orange" "solid"))

;; shape-to-tri : placed-shape -> placed-shape
;; purpose statement: changers the shape from whatever it is to a triangle

(define (shape-to-tri p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-tri (* (draw-circ-radius (placed-shape-shape p)) 2)
                    (draw-circ-color (placed-shape-shape p))
                    (draw-circ-set (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-tri (draw-tri-size (placed-shape-shape p))
                    (draw-tri-color (placed-shape-shape p))
                    (draw-tri-set (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-tri (draw-rect-width (placed-shape-shape p))
                    (draw-rect-color (placed-shape-shape p))
                    (draw-rect-set (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-tri (draw-sta-size (placed-shape-shape p))
                    (draw-sta-color (placed-shape-shape p))
                    (draw-sta-set (placed-shape-shape p)))]))

(check-expect (shape-to-tri PLACED-SHAPE1) (make-draw-tri 20 "blue" "solid"))
(check-expect (shape-to-tri PLACED-SHAPE3) (make-draw-tri 70 "orange" "solid"))
(check-expect (shape-to-tri PLACED-SHAPE4) (make-draw-tri 20 "orange" "solid"))

;; shape-to-rect : placed-shape -> placed-shape
;; purpose statement: changers the shape from whatever it is to a rectangle

(define (shape-to-rect p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-rect (- (* 2(draw-circ-radius (placed-shape-shape p))) 5)
                     (* 2(draw-circ-radius (placed-shape-shape p)))
                     (draw-circ-color (placed-shape-shape p))
                     (draw-circ-set (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-rect (- (draw-tri-size (placed-shape-shape p)) 5)
                     (draw-tri-size (placed-shape-shape p))
                     (draw-tri-color (placed-shape-shape p))
                     (draw-tri-set (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-rect (draw-rect-height (placed-shape-shape p))
                     (draw-rect-width (placed-shape-shape p))
                     (draw-rect-color (placed-shape-shape p))
                     (draw-rect-set (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-rect (- (draw-sta-size (placed-shape-shape p)) 5)
                     (draw-sta-size (placed-shape-shape p))
                     (draw-sta-color (placed-shape-shape p))
                     (draw-sta-set (placed-shape-shape p)))]))

(check-expect (shape-to-rect PLACED-SHAPE1) (make-draw-rect 15 20 "blue" "solid"))
(check-expect (shape-to-rect PLACED-SHAPE2) (make-draw-rect 10 15 "green" "solid"))
(check-expect (shape-to-rect PLACED-SHAPE4) (make-draw-rect 15 20 "orange" "solid"))

;; shape-to-sta: placed-shape -> placed-shape
;; purpose statement: changers the shape from whatever it is to a star

(define (shape-to-sta p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-sta (* 2(draw-circ-radius (placed-shape-shape p)))
                    (draw-circ-color (placed-shape-shape p))
                    (draw-circ-set (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-sta (draw-tri-size (placed-shape-shape p))
                    (draw-tri-color (placed-shape-shape p))
                    (draw-tri-set (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-sta (draw-rect-width (placed-shape-shape p))
                    (draw-rect-color (placed-shape-shape p))
                    (draw-rect-set (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-sta (draw-sta-size (placed-shape-shape p))
                    (draw-sta-color (placed-shape-shape p))
                    (draw-sta-set (placed-shape-shape p)))]))

(check-expect (shape-to-sta PLACED-SHAPE1) (make-draw-sta 20 "blue" "solid"))
(check-expect (shape-to-sta PLACED-SHAPE2) (make-draw-sta 15 "green" "solid"))
(check-expect (shape-to-sta PLACED-SHAPE3) (make-draw-sta 70 "orange" "solid"))

;; new-shape : placed-shape String -> placed-shape
;; purpose statement: changes the shape of a placed-shape

(define (new-shape p shap)
  (cond
    [(string=? "circle" shap)
     (make-placed-shape (shape-to-circ p)
                        (placed-shape-posn p)
                        (placed-shape-background p))]
    [(string=? "triangle" shap)
     (make-placed-shape (shape-to-tri p)
                        (placed-shape-posn p)
                        (placed-shape-background p))]
    [(string=? "rectangle" shap)
     (make-placed-shape (shape-to-rect p)
                        (placed-shape-posn p)
                        (placed-shape-background p))]
    [(string=? "star" shap)
     (make-placed-shape (shape-to-sta p)
                        (placed-shape-posn p)
                        (placed-shape-background p))]))


(check-expect (new-shape PLACED-SHAPE1 "star") (make-placed-shape
                                                (make-draw-sta 20 "blue" "solid")
                                                (make-posn 50 50) BACKGROUND))
(check-expect (new-shape PLACED-SHAPE2 "circle") (make-placed-shape
                                                  (make-draw-circ 7.5 "green" "solid")
                                                  (make-posn 100 100) BACKGROUND)) 
(check-expect (new-shape PLACED-SHAPE3 "triangle") (make-placed-shape
                                                    (make-draw-tri 70 "orange" "solid")
                                                    (make-posn 150 150) BACKGROUND))

;; new-color : placed-shape String -> placed-shape
;; purpose statement : changes the color of the placed-shape

(define (new-color p col)
  (make-placed-shape (cond
                       [(draw-circ? (placed-shape-shape p))
                        (make-draw-circ (draw-circ-radius (placed-shape-shape p))
                                        col
                                        (draw-circ-set (placed-shape-shape p)))]
                       [(draw-tri? (placed-shape-shape p))
                        (make-draw-tri (draw-tri-size (placed-shape-shape p))
                                       col
                                       (draw-tri-set (placed-shape-shape p)))]
                       [(draw-rect? (placed-shape-shape p))
                        (make-draw-rect (draw-rect-height (placed-shape-shape p))
                                        (draw-rect-width (placed-shape-shape p))
                                        col
                                        (draw-rect-set (placed-shape-shape p)))]
                       [(draw-sta? (placed-shape-shape p))
                        (make-draw-sta (draw-sta-size (placed-shape-shape p))
                                       col
                                       (draw-sta-set (placed-shape-shape p)))])
                     (placed-shape-posn p)
                     (placed-shape-background p)))

(check-expect (new-color PLACED-SHAPE1 "red") (make-placed-shape
                                               (make-draw-circ 10 "red" "solid")
                                               (make-posn 50 50) BACKGROUND))
(check-expect (new-color PLACED-SHAPE2 "blue") (make-placed-shape
                                                (make-draw-tri 15 "blue" "solid")
                                                (make-posn 100 100) BACKGROUND)) 
(check-expect (new-color PLACED-SHAPE3 "yellow") (make-placed-shape
                                                  (make-draw-rect 20 70 "yellow" "solid")
                                                  (make-posn 150 150) BACKGROUND))

;; add-size : placed-shape -> placed-shape
;; purpose statement: Adds five to shape size

(define (add-size p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-circ (+ 2.5(draw-circ-radius
                            (placed-shape-shape p)))
                     (draw-circ-color
                      (placed-shape-shape p))
                     (draw-circ-set
                      (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-tri (+ 5(draw-tri-size
                         (placed-shape-shape p)))
                    (draw-tri-color
                     (placed-shape-shape p))
                    (draw-tri-set
                     (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-rect (+ 5 (draw-rect-height
                           (placed-shape-shape p)))
                     (+ 5(draw-rect-width
                          (placed-shape-shape p)))
                     (draw-rect-color
                      (placed-shape-shape p))
                     (draw-rect-set
                      (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-sta (+ 5(draw-sta-size
                         (placed-shape-shape p)))
                    (draw-sta-color
                     (placed-shape-shape p))
                    (draw-sta-set
                     (placed-shape-shape p)))]))

(check-expect (add-size PLACED-SHAPE1) (make-draw-circ 12.5 "blue" "solid"))
(check-expect (add-size PLACED-SHAPE2) (make-draw-tri 20 "green" "solid"))
(check-expect (add-size PLACED-SHAPE3) (make-draw-rect 25 75 "orange" "solid"))

;; minus-size : placed-shape -> placed-shape
;; purpose statement: Subtracts five from shape size

(define (minus-size p)
  (cond
    [(draw-circ? (placed-shape-shape p))
     (make-draw-circ (- (draw-circ-radius
                         (placed-shape-shape p)) 2.5)
                     (draw-circ-color
                      (placed-shape-shape p))
                     (draw-circ-set
                      (placed-shape-shape p)))]
    [(draw-tri? (placed-shape-shape p))
     (make-draw-tri (- (draw-tri-size
                        (placed-shape-shape p)) 5)
                    (draw-tri-color
                     (placed-shape-shape p))
                    (draw-tri-set
                     (placed-shape-shape p)))]
    [(draw-rect? (placed-shape-shape p))
     (make-draw-rect (- (draw-rect-height
                         (placed-shape-shape p)) 5)
                     (- (draw-rect-width
                         (placed-shape-shape p)) 5)
                     (draw-rect-color
                      (placed-shape-shape p))
                     (draw-rect-set
                      (placed-shape-shape p)))]
    [(draw-sta? (placed-shape-shape p))
     (make-draw-sta (- (draw-sta-size
                        (placed-shape-shape p)) 5)
                    (draw-sta-color
                     (placed-shape-shape p))
                    (draw-sta-set
                     (placed-shape-shape p)))]))

(check-expect (minus-size PLACED-SHAPE1) (make-draw-circ 7.5 "blue" "solid"))
(check-expect (minus-size PLACED-SHAPE2) (make-draw-tri 10 "green" "solid"))
(check-expect (minus-size PLACED-SHAPE3) (make-draw-rect 15 65 "orange" "solid"))

;; new-size : placed-shape String -> placed-shape
;; purpose statement : changes the size of the placed-shape

(define (new-size p size)
  (cond
    [(string=? "+" size) (make-placed-shape (add-size p)
                                            (placed-shape-posn p)
                                            (placed-shape-background p))]
    [(string=? "-" size) (make-placed-shape (minus-size p)
                                            (placed-shape-posn p)
                                            (placed-shape-background p))]))

(check-expect (new-size PLACED-SHAPE1 "+") (make-placed-shape (make-draw-circ 12.5 "blue" "solid")
                                                              (make-posn 50 50) BACKGROUND))
(check-expect (new-size PLACED-SHAPE2 "-") (make-placed-shape (make-draw-tri 10 "green" "solid")
                                                              (make-posn 100 100) BACKGROUND)) 
(check-expect (new-size PLACED-SHAPE3 "+") (make-placed-shape (make-draw-rect 25 75 "orange" "solid")
                                                              (make-posn 150 150) BACKGROUND))

;; button-press? : placed-shape number -> placed-shape
;; purpose statement : determines if a button is pushed and does that button function if it is

(define (button-press? p x)
  (cond
    [(< x 30) (new-shape p "circle")]
    [(< x 50) (new-shape p "triangle")]
    [(< x 70) (new-shape p "rectangle")]
    [(< x 90) (new-shape p "star")]
    [(< x 110) (new-color p "red")]
    [(< x 130) (new-color p "orange")]
    [(< x 150) (new-color p "yellow")]
    [(< x 170) (new-color p "green")]
    [(< x 190) (new-color p "blue")]
    [(< x 210) (new-color p "violet")]
    [(< x 230) (new-size p "+")]
    [(< x 250) (new-size p "-")]
    [else (ps p)]))

(check-expect (button-press? PLACED-SHAPE1 100) (make-placed-shape (make-draw-circ 10 "red" "solid")
                                                                   (make-posn 50 50) BACKGROUND))
(check-expect (button-press? PLACED-SHAPE2 150) (make-placed-shape (make-draw-tri 15 "green" "solid")
                                                                   (make-posn 100 100) BACKGROUND))
(check-expect (button-press? PLACED-SHAPE3 200) (make-placed-shape (make-draw-rect 20 70
                                                                                   "violet" "solid")
                                                                   (make-posn 150 150) BACKGROUND))

;; fun-paint : lst -> Image
;; purpose statement: Draws the world

(define fun-paint 
  (big-bang (make-placed-shape DRAW-CIRC1 (make-posn 0 0) BACKGROUND)
    [to-draw draw-world]
    [on-mouse handle-mouse]))