(ns webwallet.crypto.bitcoinj.keys-test
  (:require [clojure.test :refer :all]
            [webwallet.crypto.bitcoinj.keys :as btcj-keys]))

;; https://en.bitcoin.it/wiki/Technical_background_of_version_1_Bitcoin_addresses
(def private-key-hex "18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725")
(def public-key-hex "0250863ad64a87ae8a2fe83c1af1a8403cb53f53e486d8511dad8a04887e5b2352")
(def address-base58 "1PMycacnJaSqwwJqjawXBErnLsZ7RkXUAs")
(def message-to-sign "hello")
(def message-signature "30450221009b82b660de94b623a1d99bbe11d36733e0fb8a510cbafde3101a7d2739c3a917022008c12dd6379afb027ebc271c608187770cfa1dc4e2bc75eeba1e3c5271bc6609")

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

(deftest signature-generation
  (testing "Generate a signature starting from a private key string"
    (let [keypair-from-private (btcj-keys/keypair-from-private-hex private-key-hex)
          generated-signature (btcj-keys/hash-twice-and-sign message-to-sign keypair-from-private)]
      (is (= generated-signature message-signature))))
  (testing "Verify a signature given its DER string and a public key string"
    (let [verification-result (btcj-keys/verify-signature-on-message message-to-sign message-signature public-key-hex)]
      (is (true? verification-result)))))
