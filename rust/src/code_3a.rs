use regex::Regex;

use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where P: AsRef<Path>, {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

pub fn main() {
    let rgx = Regex::new(r"mul\((\d{1,3}),(\d{1,3})\)").unwrap();

    let mut sum = 0;

    if let Ok(file) = read_lines("../input/input_3.txt") {
        for line in file.flatten() {
            let toks: Vec<(i32, i32)> = rgx.captures_iter(&line).map(|caps| {
                let a = caps.get(1).unwrap().as_str();
                let b = caps.get(2).unwrap().as_str();
                (a.parse().unwrap(), b.parse().unwrap())
            }).collect();
            for tok in toks {
                sum += tok.0 * tok.1
            }
        }
    }

    println!("{}", sum)
}