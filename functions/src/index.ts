import {setGlobalOptions} from "firebase-functions";
import {HttpsError, onCall} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
import {defineSecret} from "firebase-functions/params";

const allowedUserUid = defineSecret("ALLOWED_USER_UID");

setGlobalOptions({
  region: "us-east4",
  maxInstances: 1,
});

export const askAiCoach = onCall(
  {secrets: [allowedUserUid]},
  async (request) => {
    if (!request.auth) {
      throw new HttpsError(
        "unauthenticated",
        "Sign in is required to use AI Coach.",
      );
    }
    if (request.auth.uid !== allowedUserUid.value()) {
      throw new HttpsError(
        "permission-denied",
        "You are not authorized to use AI Coach.",
      );
    }
    const message = request.data?.message;

    if (typeof message !== "string" || message.trim().length === 0) {
      throw new HttpsError(
        "invalid-argument",
        "A non-empty message is required.",
      );
    }
    logger.info("AI Coach request received");

    return {
      reply: `You said: ${message}. Keep going—you can do this!`,
    };
  });
