(ns tictactoe-server.move-test
  (:require [speclj.core :refer :all]
            [webserver.response :as response]
            [tictactoe-server.app]
            [tictactoe-server.new]
            [tictactoe-server.move]
            [tictactoe-server.mock-socket :as socket])
  (:import [me.hkgumbs.tictactoe.main.java.board SquareBoard Board$Mark]))

(describe "Request to /move"
  (it "adds piece to board"
    (socket/connect "/new" "size=3&vs=naive")
    (socket/validate-body
      (socket/connect "/move" "position=4")
      {:board (.toString (-> (SquareBoard. 3)
                             (.add 4 Board$Mark/X)
                             (.add 0 Board$Mark/O)
                             (.toString)))}))
  (it "400s on invalid input"
    (socket/connect "/new" "size=3&vs=naive")
    (should=
      (response/make 400)
      (socket/connect "/move" "position=-1"))
    (should=
      (response/make 400)
      (socket/connect "/move" "position=foobar"))))