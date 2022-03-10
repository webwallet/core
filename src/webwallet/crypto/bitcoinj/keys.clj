(ns webwallet.crypto.bitcoinj.keys
  (:require [webwallet.crypto.hashing :as hashing])
  (:import (org.bitcoinj.core ECKey Utils Base58)))

(defn keypair-from-private-hex [private-hex]
  (-> (BigInteger. private-hex 16)
      (ECKey/fromPrivate)))

(defn keypair-from-public-hex [public-hex]
  (-> (.decode Utils/HEX public-hex)
      (ECKey/fromPublicOnly)))

(defn get-private-key-hex [keypair]
  (.getPrivateKeyAsHex keypair))

(defn get-public-key-hex [keypair]
  (.getPublicKeyAsHex keypair))

(defn public-from-private-hex
  ([private-hex]
   (public-from-private-string private-hex true))
  ([private-hex compressed]
   (-> (BigInteger. private-hex 16)
       (ECKey/publicKeyFromPrivate compressed))))

(defn address-from-keypair [keypair]
  (let [prefix-integer (Integer/parseInt "00" 16)]
    (->> keypair
         (.getPubKeyHash)
         (Base58/encodeChecked prefix-integer))))

(defn address-from-public-hex [public-hex]
  (let [prefix-integer (Integer/parseInt "00" 16)]
    (->> public-hex
         (.decode Utils/HEX)
         (Utils/sha256hash160)
         (Base58/encodeChecked prefix-integer))))
