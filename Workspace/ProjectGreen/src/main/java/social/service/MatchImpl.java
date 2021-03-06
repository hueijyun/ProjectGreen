package social.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import social.DAO.MatchDAO;
import social.DAO.MatchDAOImpl;
import social.bean.FriendInfoBean;
import social.bean.MatchRequestBean;
import social.bean.MatchesBean;
import social.bean.MatchingBean;

public class MatchImpl implements Match {

	MatchDAO matchDAO;

	public MatchImpl() {
		matchDAO = new MatchDAOImpl();
	}

	@Override
	public void insertMatchRequest(MatchRequestBean mrb) {
		matchDAO.insertMatchRequest(mrb);
	}

	public List<MatchingBean> todayRequest() {
		return matchDAO.todayRequest();
	};

	public void insertMatchResult(List matchResult) {
		matchDAO.insertMatchResult(matchResult);
	}

	public List<List<Integer>> getMatch(List<MatchingBean> todayRequest) {
		int amount = todayRequest.size();
		System.out.println(amount);
		List<List<Integer>> matchList = new ArrayList();
		if (amount != 0) {
			int i;
			int j;
//			System.out.println("要配對組" + amount);
			List<List<Integer>> matchedALL = new ArrayList<>();

			for (j = 0; j < amount; j++) {
				for (i = 0; i < amount; i++) {
					if (i != j) {
						List<Integer> matched = new ArrayList<>();
						int count = 0;
						if (todayRequest.get(j).getInterestedRestaurantType() != null
								&& todayRequest.get(i).getInterestedRestaurantType() != null
								&& todayRequest.get(j).getTourTypeLike() != "-1") {
							if (todayRequest.get(j).getInterestedRestaurantType()
									.equals(todayRequest.get(i).getInterestedRestaurantType())) {
								count += 2;
							}
						}
						if (todayRequest.get(j).getTourTypeLike() != null
								&& todayRequest.get(i).getTourTypeLike() != null
								&& todayRequest.get(j).getTourTypeLike() != "-1") {
							if (todayRequest.get(j).getTourTypeLike().equals(todayRequest.get(i).getTourTypeLike())) {
								count += 2;
							}
						}
						if (todayRequest.get(j).getHeightLike() != null
								&& todayRequest.get(i).getProfileHeight() != null
								&& todayRequest.get(j).getHeightLike() != -1) {
							if (todayRequest.get(j).getHeightLike().equals(todayRequest.get(i).getProfileHeight())) {
								count += 1;
							}
						}
						if (todayRequest.get(j).getWeightLike() != null
								&& todayRequest.get(i).getProfileWeight() != null
								&& todayRequest.get(j).getWeightLike() != -1) {
							if (todayRequest.get(j).getWeightLike().equals(todayRequest.get(i).getProfileWeight())) {
								count += 1;
							}
						}
						if (todayRequest.get(j).getGenderLike() != null && todayRequest.get(i).getGender() != null
								&& todayRequest.get(j).getGenderLike() != -1) {
							if (todayRequest.get(j).getGenderLike().equals(todayRequest.get(i).getGender())) {
								count += 2;
							}
						}
						if (todayRequest.get(j).getAgeBottomLike() != null
								&& todayRequest.get(j).getAgeTopLike() != null
								&& todayRequest.get(i).getMemberBirthday() != null
								&& (todayRequest.get(j).getAgeBottomLike() != -1
										&& todayRequest.get(j).getAgeTopLike() != -1)) {
							Date birthday = new Date(todayRequest.get(i).getMemberBirthday().getTime());
							int age = getAge(birthday);
							int bottom = todayRequest.get(j).getAgeBottomLike();
							int top = todayRequest.get(j).getAgeTopLike();
							if (todayRequest.get(j).getAgeBottomLike() == -1) {
								bottom = 18;
							}
							if (todayRequest.get(j).getAgeTopLike() == -1) {
								top = 100;
							}
							if (bottom < age && age < top) {
								count += 2;
							}
						}
//						System.out.println("todayRequest.get(j).getMemberID()" + todayRequest.get(j).getMemberID()
//								+ "todayRequest.get(i).getMemberID()" + todayRequest.get(i).getMemberID());
						Boolean b11 = todayRequest.get(j).getMemberID() > todayRequest.get(i).getMemberID();
//						System.out.println(b11);
						if (b11) {
							matched.add(todayRequest.get(i).getMemberID());
							matched.add(todayRequest.get(j).getMemberID());
						} else {
							matched.add(todayRequest.get(j).getMemberID());
							matched.add(todayRequest.get(i).getMemberID());
						}
						matched.add(count);
						matchedALL.add(matched);

//					System.out.println("matchedALL" + matchedALL.size());
						// 殺掉重複的，並將兩人速配指數相加除以二
						for (int k = 0; k < matchedALL.size(); k++) {
							boolean b1 = (matched.get(1).equals(matchedALL.get(k).get(0)));
							boolean b2 = (matched.get(0).equals(matchedALL.get(k).get(1)));
							if (b1 && b2) {
								matchedALL.get(k).set(2, (matched.get(2) + matchedALL.get(k).get(2)) / 2);
								matchedALL.remove(matchedALL.size() - 1);
							}
						}
						matched = null;
//					System.out.println("matchedALL2" + matchedALL.size());
					}
				}
			}

			List pair1 = new ArrayList();
			while (matchedALL.size() != 0) {
				Integer topScore = 0;
				Integer a2 = 0;
				List chose2 = new ArrayList();
				int matchBase;
				// 找最高分
				for (List<Integer> ar : matchedALL) {
					List chose = new ArrayList();
					if (ar.get(2) > topScore) {
						topScore = ar.get(2);
					}
				}
//			System.out.println("最高分" + topScore);
				// 抓最高分配對
				for (List<Integer> ar2 : matchedALL) {
					if (ar2.get(2) == topScore) {
						chose2.add(ar2);
					}
				}
				// 取配對
				matchBase = (int) (Math.random() * chose2.size());
				pair1 = ((List) chose2.get(matchBase));
				matchList.add(pair1);
				// 殺掉已配對的組合
				for (int de = 0; de < matchedALL.size();) {
					boolean p1 = (pair1.get(0).equals(matchedALL.get(de).get(0)));
					boolean p2 = (pair1.get(0).equals(matchedALL.get(de).get(1)));
					boolean p3 = (pair1.get(1).equals(matchedALL.get(de).get(0)));
					boolean p4 = (pair1.get(1).equals(matchedALL.get(de).get(1)));
					if (p1 || p2 || p3 || p4) {
						matchedALL.remove(de);
						de -= 1;
					}
					de++;
				}
			}
			System.out.println("組數" + matchList.size());
			// 整理陣列，方便insert Matches
			for (int f = 0; f < matchList.size(); f++) {
				if (matchList.get(f).get(0) > matchList.get(f).get(1)) {
					matchList.get(f).add(matchList.get(f).get(0));
					matchList.get(f).remove(0);
					matchList.get(f).add(1, matchList.get(f).get(2));
					matchList.get(f).remove(matchList.size() - 1);
				}
			}
		}
		return matchList;
	}

	public int getAge(Date birthday) {
		// Calendar：日歷
		/* 從Calendar對象中或得一個Date對象 */
		Calendar cal = Calendar.getInstance();
		/* 把出生日期放入Calendar類型的bir對象中，進行Calendar和Date類型進行轉換 */
		Calendar bir = Calendar.getInstance();
		bir.setTime(birthday);
		/* 如果生日大於當前日期，則拋出異常：出生日期不能大於當前日期 */
		if (cal.before(birthday)) {
			throw new IllegalArgumentException("The birthday is before Now,It‘s unbelievable");
		}
		/* 取出當前年月日 */
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayNow = cal.get(Calendar.DAY_OF_MONTH);
		/* 取出出生年月日 */
		int yearBirth = bir.get(Calendar.YEAR);
		int monthBirth = bir.get(Calendar.MONTH);
		int dayBirth = bir.get(Calendar.DAY_OF_MONTH);
		/* 大概年齡是當前年減去出生年 */
		int age = yearNow - yearBirth;
		/* 如果出當前月小與出生月，或者當前月等於出生月但是當前日小於出生日，那麽年齡age就減一歲 */
		if (monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)) {
			age--;
		}
		return age;
	}

	public void markMatch(Integer i) {
		matchDAO.markMatch(i);
	}

	public void markPairDate(Integer i) {
		matchDAO.markPairDate(i);
	}

	public List<MatchesBean> showMatch() {
		return matchDAO.showMatch();
	}

	public List<MatchesBean> showFriends() {
		List<MatchesBean> mb = matchDAO.showMatch();
		for (int i = 0; i < mb.size(); i++) {
//			System.out.println(mb.get(i).getFriendDate());
			if (mb.get(i).getDelete1() == 1 || mb.get(i).getFriendDate() == null) {
				mb.remove(i);
				i--;
			}
		}
		return mb;
	}

	public List<MatchesBean> showTodayMatch() {
		List<MatchesBean> mb = matchDAO.showMatch();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < mb.size(); i++) {
			String matchday = sdf.format(mb.get(i).getPairDate());
//			System.out.println(matchday);
			String today = sdf.format(new Date());
//			System.out.println(today);
			if (!matchday.equals(today) || mb.get(i).getFriendDate() != null) {
				mb.remove(i);
				i--;
			}
		}
		return mb;
	}

	public List<MatchesBean> showFriend() {
		List<MatchesBean> mb = matchDAO.showMatch();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < mb.size(); i++) {
			String matchday = sdf.format(mb.get(i).getPairDate());
//			System.out.println(matchday);
			String today = sdf.format(new Date());
//			System.out.println(today);
			if (mb.get(i).getFriendDate() == null) {
				mb.remove(i);
				i--;
			}
		}
		return mb;
	}

	public FriendInfoBean showFriendInfo(Integer i) {
		return matchDAO.showFriendInfo(i);
	}

	public void deleteFriend(Integer i) {
		matchDAO.deleteFriend(i);
	}
}