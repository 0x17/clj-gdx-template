(ns clj-gdx-template.core
  (:import (com.badlogic.gdx Game Gdx Screen Input Input$Keys)
           (com.badlogic.gdx.graphics OrthographicCamera GL10)
           (com.badlogic.gdx.graphics.glutils ImmediateModeRenderer10)
           (com.badlogic.gdx.math Vector3)
           (com.badlogic.gdx.backends.lwjgl LwjglApplication)))

(def triangle-coords '([0 0 0] [200 0 0] [100 150 0]))
(def triangle-vec3s (map #(Vector3. (float-array %)) triangle-coords))

(def orthoCam (atom nil))
(def imr (atom nil))

(defn draw-triangle []
  (.begin @imr GL10/GL_TRIANGLES)
  (doseq [vec3 triangle-vec3s] (.vertex @imr vec3))
  (.end @imr))

(defn draw [delta]
  (.glClear Gdx/gl GL10/GL_COLOR_BUFFER_BIT)
  (.apply @orthoCam Gdx/gl)
  (draw-triangle))

(defn exit-on-escape [] (if (.isKeyPressed Gdx/input Input$Keys/ESCAPE) (.exit Gdx/app)))

(defn main-screen []
  (proxy [Screen] []
    (show []
      (reset! orthoCam (OrthographicCamera. (.getWidth Gdx/graphics) (.getHeight Gdx/graphics)))
      (reset! imr (ImmediateModeRenderer10.)))
    (dispose []
      (.dispose @imr))
    (render [delta]
      (do
        (exit-on-escape)
        (draw delta)))
    (resize [w h])
    (hide [])
    (pause [])
    (resume [])))

(defn main-game [] (proxy [Game] [] (create [] (.setScreen this (main-screen)))))

(defn -main [& args] (LwjglApplication. (main-game) "Test" 800 480 false))