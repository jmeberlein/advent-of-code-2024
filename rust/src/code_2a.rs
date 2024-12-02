use itertools::Itertools;

use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where P: AsRef<Path>, {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

fn is_safe(report: Vec<i32>) -> bool {
    let mut diffs = Vec::new();
    for (a, b) in report.into_iter().tuple_windows() {
        diffs.push(b - a);
    }
    let all_small = diffs.iter().map(|&i| i.abs() <= 3).reduce(|a,b| a && b).unwrap();
    let all_positive = diffs.iter().map(|&i| i > 0).reduce(|a,b| a && b).unwrap();
    let all_negative = diffs.iter().map(|&i| i < 0).reduce(|a,b| a && b).unwrap();
    (all_positive || all_negative) && all_small
}

pub fn main() {
    let mut count = 0;

    if let Ok(file) = read_lines("../input/input_2.txt") {
        for line in file.flatten() {
            let toks: Vec<&str> = line.split_whitespace().collect();
            let mut report = Vec::new();
            for tok in toks {
                report.push(tok.parse::<i32>().unwrap());
            }
            if is_safe(report) {
                count += 1;
            }
        }
    }

    println!("{}", count)
}