import {setGlobalOptions} from "firebase-functions";
import {HttpsError, onCall} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";

setGlobalOptions({
  region: "us-east4",
  maxInstances: 1,
});

export const askAiCoach = onCall(async (request) => {
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
