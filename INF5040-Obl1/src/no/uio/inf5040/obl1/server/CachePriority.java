package no.uio.inf5040.obl1.server;

public class CachePriority {
	UserPlayCount first;
	int lowest, numElements;

	CachePriority() {
		first = null;
		lowest = 0;
	}

	private class UserPlayCount {
		String userId;
		int playCount;

		UserPlayCount next, prev;

		UserPlayCount(String userId, int playCount) {
			this.userId = userId;
			this.playCount = playCount;
		}
	}

	void add(String userId, int playCount) {
		UserPlayCount in = new UserPlayCount(userId, playCount);

		if (first == null)
			first = in;

		else {
			UserPlayCount temp = first;

			while (in.playCount > temp.playCount)
				temp = temp.next;

			in.next = temp;
			in.prev = temp.prev;
			if (temp.prev != null)
				temp.prev.next = in;
			temp.prev = in;
		}

		lowest = first.playCount;
	}

	String pop() {
		String toReturn = first.userId;
		first = first.next;
		first.prev = null;
		lowest = first.playCount;

		return toReturn;
	}

	int getLowest() {
		return first.playCount;
	}
}
