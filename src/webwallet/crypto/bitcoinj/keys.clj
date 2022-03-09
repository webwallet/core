(ns webwallet.crypto.bitcoinj.keys
  (:require [buddy.core.codecs :refer [bytes->hex]])
  (:import (org.bitcoinj.core ECKey)))

(defn public-from-private-string
  ([private-string]
   (public-from-private-string private-string true))
  ([private-string compressed]
   (let [biginteger-from-hex (BigInteger. private-string 16)
         public-from-private (ECKey/publicKeyFromPrivate biginteger-from-hex compressed)]
     (bytes->hex public-from-private))))
