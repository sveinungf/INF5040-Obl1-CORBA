package no.uio.inf5040.obl1.server;

/**
 * 
 * 
 * Temporary data structure used while caching to keep tabs on the
 * users currently cached
 * Structure is a sorted linked list
 * with least active user at the front of the list
 * 
 * @author halvor
 * 
 */
public class CachePriority {
	private UserPlayCount first;
	private int lowest;

	CachePriority() {
		first = null;
		lowest = 0;
	}

	/**
	 * 
	 * Inner class used to store info
	 * about a users play count
	 * 
	 * @author halvor
	 */
	private class UserPlayCount {
		private String userId;
		private int playCount;

		UserPlayCount next, prev;

		UserPlayCount(String userId, int playCount) {
			this.userId = userId;
			this.playCount = playCount;
		}
	}

	/**
	 * Adds info about a users play count to the list
	 * <br>Users are sorted in ascending order according to total play count
	 *  
	 * @param userId - ID of the user to add
	 * @param playCount - users total play count
	 */
	void add(String userId, int playCount) {
		UserPlayCount in = new UserPlayCount(userId, playCount);

		if (first == null)
			first = in;

		else {
			UserPlayCount temp = first;

			while (in.playCount > temp.playCount) {
				if (temp.next == null)
					break;
				temp = temp.next;
			}

			if (temp.next == null) {
				temp.next = in;
				in.prev = temp;
			}

			else {
				in.next = temp;
				in.prev = temp.prev;
				if (temp.prev != null)
					temp.prev.next = in;
				temp.prev = in;
			}
		}
		lowest = first.playCount;
	}

	
	/**
	 * Removes the first user in the list (the least active user)
	 * @return ID of the user that was removed
	 */
	String pop() {
		String toReturn = first.userId;
		first = first.next;

		if (first != null) {
			first.prev = null;
			lowest = first.playCount;
		} else
			lowest = 0;

		return toReturn;
	}

	
	/**
	 * @return least active user's play count
	 */
	int getLowest() {
		return lowest;
	}
}
