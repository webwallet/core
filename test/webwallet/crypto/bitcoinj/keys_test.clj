(ns webwallet.crypto.bitcoinj.keys-test
  (:require [clojure.test :refer :all]
            [webwallet.crypto.bitcoinj.keys :as btcj-keys]))

;; https://en.bitcoin.it/wiki/Technical_background_of_version_1_Bitcoin_addresses
(def private-key-hex "18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725")
(def public-key-hex "0250863ad64a87ae8a2fe83c1af1a8403cb53f53e486d8511dad8a04887e5b2352")
(def address-base58 "1PMycacnJaSqwwJqjawXBErnLsZ7RkXUAs")

;; "key pair" is an object
(deftest keypair-generation
  (testing "Generate key pair from private key string"
    (let [keypair-from-private (btcj-keys/keypair-from-private-hex private-key-hex)
          private-from-keypair (btcj-keys/get-private-key-hex keypair-from-private)
          public-from-keypair (btcj-keys/get-public-key-hex keypair-from-private)]
      (is (= private-from-keypair private-key-hex))
      (is (= public-from-keypair public-key-hex))))
  (testing "Derive public key from private key string"
    (let [public-from-private (btcj-keys/public-from-private-hex private-key-hex)]
      (is (= public-from-private public-key-hex))))
  (testing "Generate address from public key string"
    (let [keypair-from-public (btcj-keys/keypair-from-public-hex public-key-hex)
          address-from-keypair (btcj-keys/address-from-keypair keypair-from-public)
          address-from-public (btcj-keys/address-from-public-hex public-key-hex)]
      (is (= address-from-keypair address-base58))
      (is (= address-from-public address-base58)))))
