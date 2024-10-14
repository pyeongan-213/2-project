package kr.co.duck.beans;

public class PageBean {

	// 최소 페이지 번호
	private int min;
	// 최대 페이지 번호
	private int max;
	// 이전 버튼 페이지 번호
	private int prevPage;
	// 다음 버튼 페이지 번호
	private int nextPage;
	// 전체 페이지 갯수
	private int pageCnt;
	// 현재 페이지 번호
	private int currentPage;

	// 전체글 갯수, 현재 페이지 번호, 페이지 당 글 갯수(10), 페이지 버튼 갯수
	public PageBean(int contentCnt, int currentPage, int contentPageCnt, int paginationCnt) {

		this.currentPage = currentPage;

		// 전체 페이지 갯수 = 전체글 갯수/페이지당 글 갯수
		pageCnt = contentCnt / contentPageCnt;

		if (contentCnt % contentPageCnt > 0) {
			pageCnt++;
		}

		min = ((currentPage - 1) / paginationCnt) * paginationCnt + 1;
		max = min + paginationCnt - 1;

		if (max > pageCnt) {
			max = pageCnt;
		}

		prevPage = min - 1;
		nextPage = max + 1;

		if (nextPage > pageCnt) {
			nextPage = pageCnt;
		}
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPageCnt() {
		return pageCnt;
	}

	public void setPageCnt(int pageCnt) {
		this.pageCnt = pageCnt;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
