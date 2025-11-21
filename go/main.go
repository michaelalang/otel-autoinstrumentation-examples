package main

import (
	"fmt"
	"log"
	"net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
	log.Printf("INFO: Request received on path: %s", r.URL.Path)

	w.Header().Set("Content-Type", "text/plain; charset=utf-8")
	w.WriteHeader(http.StatusOK)

	fmt.Fprintf(w, "Hello World")
}

func exceptionHandler(w http.ResponseWriter, r *http.Request) {
	log.Printf("WARNING: Request received on path: %s. Triggering panic now...", r.URL.Path)
	panic("A controlled, deliberate panic was initiated by the /exception endpoint.")
}

func main() {
	http.HandleFunc("/", helloHandler)
	http.HandleFunc("/exception", exceptionHandler)

	const port = "8080"
	log.Printf("Listening on http://0.0.0.0:%s", port)

	if err := http.ListenAndServe(":"+port, nil); err != nil {
		log.Fatalf("FATAL: Server failed to start: %v", err)
	}
}
