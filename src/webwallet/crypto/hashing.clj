(ns webwallet.crypto.hashing
  (:require [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [hex->bytes bytes->hex str->bytes]]
            [alphabase.base58 :as base58]))

(defn hash-hex [hash-algo hex-string]
  (-> hex-string
      (hex->bytes)
      (hash-algo)
      (bytes->hex)))

(defn base58check
  ([hash]
   (base58check hash "00"))
  ([hash prefix]
   (->> hash
        (str prefix)
        (hash-hex hash/sha256)
        (hash-hex hash/sha256)
        (#(subs % 0 8))
        (str prefix hash)
        (hex->bytes)
        (base58/encode))))

(comment
  (->> "0250863ad64a87ae8a2fe83c1af1a8403cb53f53e486d8511dad8a04887e5b2352"
       (hash-hex hash/sha256)
       (hash-hex hash/ripemd160)
       (base58check))

  (let [decoded "00f54a5851e9372b87810a8e60cdd2e7cfd80b6e31c7f18fe8"
        unprefixed (subs decoded 2)
        [hash checksum] (into [] (comp (partition-all 40) (map (partial apply str))) unprefixed)]
    [hash checksum])

  (into [] (map (partial apply str) (partition-all 42 "00f54a5851e9372b87810a8e60cdd2e7cfd80b6e31c7f18fe8")))
  (into [] (comp (partition-all 42) (map (partial apply str))) "00f54a5851e9372b87810a8e60cdd2e7cfd80b6e31c7f18fe8")

  (base58check "f54a5851e9372b87810a8e60cdd2e7cfd80b6e31")
  (base58/encode (hex->bytes "00f54a5851e9372b87810a8e60cdd2e7cfd80b6e31c7f18fe8"))
  (bytes->hex (base58/decode "1PMycacnJaSqwwJqjawXBErnLsZ7RkXUAs"))
  )