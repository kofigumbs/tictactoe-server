(ns tictactoe-server.move-test
  (:require [speclj.core :refer :all]
            [webserver.response :as response]
            [tictactoe-server.new]
            [tictactoe-server.move]
            [tictactoe-server.mock-socket :as socket])
  (:import [me.hkgumbs.tictactoe.main.java.board SquareBoard Board$Mark]))

(describe "Naive CPU"
  (it "adds piece to board in first slot"
    (socket/connect "/new" "size=3&vs=naive")
    (socket/validate-body
      (socket/connect "/move" "position=4")
      {:status "ready"
       :board (-> (SquareBoard. 3)
                  (.add 4 Board$Mark/X)
                  (.add 0 Board$Mark/O) .toString)}))
  (it "loses"
    (socket/connect "/new" "size=3&vs=naive")
    (socket/connect "/move" "position=8")
    (socket/connect "/move" "position=7")
    (socket/validate-body
      (socket/connect "/move" "position=6")
      {:status "terminated"
       :board (-> (SquareBoard. 3)
                  (.add 8 Board$Mark/X) (.add 0 Board$Mark/O)
                  (.add 7 Board$Mark/X) (.add 1 Board$Mark/O)
                  (.add 6 Board$Mark/X) .toString)})))

(describe "Minimax"
  (it "adds piece at best slot"
    (socket/connect "/new" "size=3&vs=minimax")
    (socket/validate-body
      (socket/connect "/move" "position=0")
      {:status "ready"
       :board (-> (SquareBoard. 3)
                  (.add 0 Board$Mark/X)
                  (.add 4 Board$Mark/O) .toString)})))

(describe "Local human"
  (it "has chance to respond with move"
    (socket/connect "/new" "size=3&vs=local")
    (socket/validate-body
      (socket/connect "/move" "position=0")
      {:status "ready"
       :board (-> (SquareBoard. 3)
                  (.add 0 Board$Mark/X) .toString)})
    (socket/validate-body
      (socket/connect "/move" "position=8")
      {:status "ready"
       :board (-> (SquareBoard. 3)
                  (.add 0 Board$Mark/X)
                  (.add 8 Board$Mark/O) .toString)})))

(describe "Invalid input to /move"
  (it "400s"
    (socket/connect "/new" "size=3&vs=naive")
    (should=
      (response/make 400)
      (socket/connect "/move" "position=-1"))
    (should=
      (response/make 400)
      (socket/connect "/move" "position=foobar"))))
