(ns webwallet.crypto.bitcoinj.keys
  (:require [webwallet.crypto.hashing :as hashing])
  (:import (org.bitcoinj.core ECKey Utils Base58 Sha256Hash)))

(defn bytes->hex [bytes]
  (.encode Utils/HEX bytes))

(defn hex->bytes [hex]
  (.decode Utils/HEX hex))

(defn keypair-from-private-hex [private-hex]
  (-> (BigInteger. private-hex 16)
      (ECKey/fromPrivate)))

(defn keypair-from-public-hex [public-hex]
  (-> (hex->bytes public-hex)
      (ECKey/fromPublicOnly)))

(defn get-private-key-hex [keypair]
  (.getPrivateKeyAsHex keypair))

(defn get-public-key-hex [keypair]
  (.getPublicKeyAsHex keypair))

(defn public-from-private-hex
  ([private-hex]
   (public-from-private-hex private-hex true))
  ([private-hex compressed]
   (-> (BigInteger. private-hex 16)
       (ECKey/publicKeyFromPrivate compressed)
       (bytes->hex))))

(defn address-from-keypair [keypair]
  (let [prefix-integer (Integer/parseInt "00" 16)]
    (->> (.getPubKeyHash keypair)
         (Base58/encodeChecked prefix-integer))))

(defn address-from-public-hex [public-hex]
  (let [prefix-integer (Integer/parseInt "00" 16)]
    (->> (hex->bytes public-hex)
         (Utils/sha256hash160)
         (Base58/encodeChecked prefix-integer))))

(defn hash-twice-and-sign [message keypair]
  (->> (.getBytes message)
       (Sha256Hash/twiceOf)
       (.sign keypair)
       (.encodeToDER)
       (bytes->hex)))
