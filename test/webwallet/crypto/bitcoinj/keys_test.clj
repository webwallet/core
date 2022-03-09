(ns webwallet.crypto.bitcoinj.keys-test
  (:require [clojure.test :refer :all]
            [webwallet.crypto.bitcoinj.keys :refer :all]))

;; https://en.bitcoin.it/wiki/Technical_background_of_version_1_Bitcoin_addresses
(def private-key-string "18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725")
(def public-key-string "0250863ad64a87ae8a2fe83c1af1a8403cb53f53e486d8511dad8a04887e5b2352")

(deftest keypair-generation
  (testing "Generate public key string from private key string"
    (let [public-from-private (public-from-private-string private-key-string)]
      (is (= public-from-private public-key-string)))))
